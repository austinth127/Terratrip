package road.trip.clients.geoapify;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import road.trip.api.location.response.LocationResponse;
import road.trip.clients.LocationRecommendationClient;
import road.trip.clients.geoapify.response.Feature;
import road.trip.clients.geoapify.response.FeatureCollection;
import road.trip.persistence.models.Location;
import road.trip.util.exceptions.UnauthorizedException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static road.trip.util.UtilityFunctions.doGet;

@Log4j2
@Service
@Qualifier("geoapify")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeoApifyClient implements LocationRecommendationClient {

    private static final String BASE_URL = "https://api.geoapify.com";
    private static final Integer THREAD_POOL_SIZE = 25;

    @Value("${geoapify-key}")
    private String API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final List<Callable<Set<LocationResponse>>> futures = new ArrayList<>();

    private URI buildUri(String path, List<NameValuePair> queryParams) {
        try {
            return new URIBuilder(BASE_URL)
                .setPath(path)
                .addParameters(queryParams)
                .build();
        } catch (URISyntaxException e) {
            log.error(e);
            return null;
        }
    }

    public Set<LocationResponse> executeAsync() throws InterruptedException {
        // Create a thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Helper function to extract the result of a future
        Function<Future<Set<LocationResponse>>, Set<LocationResponse>> getFutureResult = f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
                return null;
            }
        };

        // Collect the results into a single set of location responses
        Set<LocationResponse> locations = executorService.invokeAll(futures).stream()
            .map(getFutureResult)           // convert each future into a set of LocationResponses
            .filter(Objects::nonNull)       // skip null sets
            .flatMap(Collection::stream)    // convert the stream of sets into a flat stream
            .collect(Collectors.toSet());   // convert the stream to a set

        // Prepare for the next batch of location recommendation requests
        futures.clear();

        return locations;
    }

    public void getRecommendedLocationsAsync(Double lat, Double lon, Double radius, Set<String> categories, Integer limit) {
        futures.add(() -> getRecommendedLocations(lat, lon, radius, categories, limit));
    }

    @Override
    public LocationResponse getLocationDetails(LocationResponse location) {
        try {
            URI placeDetailsUri = buildUri("/v2/place-details", List.of(
                new BasicNameValuePair("features", "details"),
                new BasicNameValuePair("id", location.getGeoapifyId()),
                new BasicNameValuePair("apiKey", "a9b12a2a2ae0491cb7874bbf0fab7115")
            ));
            String jsonBody = doGet(client, placeDetailsUri);
            Feature feature = mapper.readValue(jsonBody, FeatureCollection.class).getFeatures().get(0);
            return new LocationResponse(feature.getProperties());
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    /**
     * radius - radius of search circle in meters
     * lat and lon are flipped...
     */
    @Override
    public Set<LocationResponse> getRecommendedLocations(Double lon, Double lat, Double radius, Set<String> categories, Integer limit) {
        log.debug(lat + " " + lon + " " + radius + " " + categories + " " + limit);
        URI placesUri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("categories", String.join(",", categories)),
            new BasicNameValuePair("filter", "circle:" + lon + "," + lat + "," + radius),
            new BasicNameValuePair("limit", limit + ""),
            new BasicNameValuePair("apiKey", API_KEY)));

        log.debug(placesUri);
        FeatureCollection places;
        try {
            String jsonBody = doGet(client, placesUri);
            log.debug(jsonBody);
            places = mapper.readValue(jsonBody, FeatureCollection.class);
        } catch (Exception e) {
            log.error(e);
            return null;
        }

        // For each returned feature, generate a location response
        Set<LocationResponse> locationResponses = new HashSet<>();
        for(Feature feature : places.getFeatures()){
            locationResponses.add(new LocationResponse(feature.getProperties()));
        }

        return locationResponses;
    }

}
