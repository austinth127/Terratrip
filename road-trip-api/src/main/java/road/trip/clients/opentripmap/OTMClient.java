package road.trip.clients.opentripmap;

import com.fasterxml.jackson.core.type.TypeReference;
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
import road.trip.api.location.response.LocationResponse;
import road.trip.clients.LocationRecommendationClient;
import road.trip.clients.opentripmap.response.OTMFullResponse;
import road.trip.clients.opentripmap.response.OTMReducedResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static road.trip.util.UtilityFunctions.doGet;

/**
 * Check this out: https://opentripmap.io/docs
 */
@Log4j2
@Service
@Qualifier("otm")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OTMClient implements LocationRecommendationClient {

    private static final String BASE_URL = "http://api.opentripmap.com";
    private static final String BASE_PATH = "/0.1/en/places";

    @Value("${otm-key}")
    private String API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private URI buildUri(String path, List<NameValuePair> queryParams) {
        try {
            return new URIBuilder(BASE_URL)
                .setPath(BASE_PATH + path)
                .addParameters(queryParams)
                .build();
        } catch (URISyntaxException e) {
            log.error(e);
            return null;
        }
    }

    private OTMFullResponse getLocationDetails(String otmId) {
        URI detailsUri = buildUri(format("/xid/%s", otmId), List.of(
            new BasicNameValuePair("apikey", API_KEY)
        ));
        OTMFullResponse response;
        try {
            String jsonBody = doGet(client, detailsUri);
            response = mapper.readValue(jsonBody, OTMFullResponse.class);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return response;
    }

    private List<OTMReducedResponse> getRecommendedOTMLocations(Double lon, Double lat, Double radius, Collection<String> categories, Integer limit) {
        URI radiusUri = buildUri("/radius", List.of(
            new BasicNameValuePair("apikey", API_KEY),
            new BasicNameValuePair("radius", radius.toString()),
            new BasicNameValuePair("lat", lat.toString()),
            new BasicNameValuePair("lon", lon.toString()),
            new BasicNameValuePair("kinds", String.join(",", categories)),
            new BasicNameValuePair("limit", limit.toString()),
            new BasicNameValuePair("format", "json")
        ));

        List<OTMReducedResponse> responses;
        try {
            String jsonBody = doGet(client, radiusUri);
            responses = mapper.readValue(jsonBody, new TypeReference<>(){});
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        return responses;
    }

    @Override
    public Set<LocationResponse> getRecommendedLocations(Double lon, Double lat, Double radius, Set<String> categories, Integer limit) {
        return getRecommendedOTMLocations(lon, lat, radius, categories, limit).stream()
            .map(otmReducedResponse -> {
                Long osmId;
                try {
                    osmId = Long.parseLong(otmReducedResponse.getOsm().split("/")[1]);
                } catch (NullPointerException | NumberFormatException e) {
                    osmId = null;
                }
                return LocationResponse.builder()
                        .categories(Arrays.asList(otmReducedResponse.getCategories().split(",")))
                        .center(new Double[]{otmReducedResponse.getPoint().getLat(), otmReducedResponse.getPoint().getLon()})
                        .otmId(otmReducedResponse.getOtmId())
                        .osmId(osmId)
                        .name(otmReducedResponse.getName())
                        .build();
                }
            ).collect(Collectors.toSet());
    }

    @Override
    public LocationResponse getLocationDetails(LocationResponse location) {
        OTMFullResponse r = getLocationDetails(location.getOtmId());
        Long osmId;
        try {
            osmId = Long.parseLong(r.getOsm().split("/")[1]);
        } catch (NullPointerException | NumberFormatException e) {
            osmId = null;
        }
        if (r.getOtmId().equals("R11077079")) {
            log.debug(r);
        }
        return LocationResponse.builder()
            .otmId(r.getOtmId())
            .osmId(osmId)
            .name(r.getName())
            .address(r.getAddress().toString())
            .categories(Arrays.asList(r.getCategories().split(",")))
            .center(new Double[]{r.getPoint().getLat(), r.getPoint().getLon()})
            .image(r.getImage() != null ? r.getImage().toString() : null)
            .website(r.getWebsite())
            .description(r.getDescription() != null ? r.getDescription().toString() : null)
            .build();
    }

}
