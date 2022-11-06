package road.trip.clients.geoapify;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import road.trip.api.location.response.LocationResponse;
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
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeoApifyClient {

    private static final String BASE_URL = "https://api.geoapify.com";

    @Value("${geoapify-key}")
    private String API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();
    private final LocationMapper locationMapper;
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private URI buildUri(String path) {
        try {
            return new URIBuilder(BASE_URL)
                .setPath(path)
                .build();
        } catch (URISyntaxException e) {
            log.error(e);
            return null;
        }
    }

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

    private String doGet(URI uri) throws Exception {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(uri)
            .GET()
            .build();
        HttpResponse<String> httpResponse = client.send(httpRequest, BodyHandlers.ofString());

        if(httpResponse.statusCode() == 401){
            throw new UnauthorizedException();
        }

        return httpResponse.body();
    }

    //TODO: test
    public List<Location> getLocationsByName(String name, int limit, double lon1, double lat1, double lon2, double lat2){
        URI uri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("name", name),
            new BasicNameValuePair("limit", "" + limit),
            new BasicNameValuePair("filter", "rect:" + lon1 + "," + lat1 + "," + lon2 + "," + lat2),
            new BasicNameValuePair("apiKey", "a9b12a2a2ae0491cb7874bbf0fab7115")));
        System.out.println(uri.toString());
        try {
            String jsonBody = doGet(uri);
            System.out.println(jsonBody);
            return locationMapper.getLocationsFromJSON(jsonBody);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    //TODO: test
    public List<Location> getLocationsByCoords(double lon, double lat, double radius){
        URI uri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("filter", "circle:" + lon + "," + lat + "," + radius),
            new BasicNameValuePair("limit", "20"),
            new BasicNameValuePair("apiKey", "a9b12a2a2ae0491cb7874bbf0fab7115")));
        try {
            String jsonBody = doGet(uri);
            return locationMapper.getLocationsFromJSON(jsonBody);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    //https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=rect%3A10.716463143326969%2C48.755151258420966%2C10.835314015356737%2C48.680903341613316&limit=20&apiKey=a9b12a2a2ae0491cb7874bbf0fab7115
    /**
     * radius - radius of search circle in meters
     */
    public List<Location> getRecommendedLocations(Double lon, Double lat, Double radius, List<String> categories, Integer limit) {
        log.debug(lon + " " + lat + " " + radius + " " + categories + " " + limit);
        URI placesUri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("categories", String.join(",", categories)),
            new BasicNameValuePair("filter", "circle:" + lat + "," + lon + "," + radius),
            new BasicNameValuePair(limit + "", "10"),
            new BasicNameValuePair("apiKey", "a9b12a2a2ae0491cb7874bbf0fab7115"))); //API_KEY

        log.info(placesUri);
        FeatureCollection places;
        try {
            String jsonBody = doGet(placesUri);
            log.debug(jsonBody);
            places = mapper.readValue(jsonBody, FeatureCollection.class);
        } catch (Exception e) {
            log.error(e);
            return null;
        }

        places.getFeatures().forEach(f -> log.info(f.getProperties().getPlaceId()));

        return places.getFeatures().parallelStream()
            .map(feature -> {
                try {
                    URI placeDetailsUri = buildUri("/v2/place-details", List.of(
                        new BasicNameValuePair("features", "details"),
                        new BasicNameValuePair("id", feature.getProperties().getPlaceId()),
                        new BasicNameValuePair("apiKey", "a9b12a2a2ae0491cb7874bbf0fab7115")
                    ));
                    String jsonBody = doGet(placeDetailsUri);
                    return mapper.readValue(jsonBody, FeatureCollection.class).getFeatures().get(0);
                } catch (Exception e) {
                    log.error(e);
                    return feature;
                }
            })
            .map(Feature::buildLocation)
            .collect(Collectors.toList());
    }

    public static void main(String args[]){
        System.out.println("Hello World");
        GeoApifyClient g = new GeoApifyClient(new LocationMapper());
        List<Location> r = g.getRecommendedLocations(-74.0060,40.7128, 1000.0, List.of("accommodation", "activity"), 20);
        System.out.print("Done");
    }
}
