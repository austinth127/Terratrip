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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import road.trip.api.location.response.LocationResponse;
import road.trip.clients.opentripmap.response.OTMFullResponse;
import road.trip.clients.opentripmap.response.OTMReducedResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static road.trip.util.UtilityFunctions.doGet;

/**
 * Check this out: https://opentripmap.io/docs
 */
@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OTMClient {

    private static final String BASE_URL = "http://api.opentripmap.com/0.1/en/places";
    private static final Integer THREAD_POOL_SIZE = 25;

    @Value("${otm-key}")
    private String API_KEY;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private final List<Callable<Set<OTMFullResponse>>> fullFutures = new ArrayList<>();
    private final List<Callable<Set<OTMReducedResponse>>> reducedFutures = new ArrayList<>();

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

    public OTMFullResponse getLocationDetails(String otmId) {
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

    public List<OTMReducedResponse> getRecommendedLocations(Double lat, Double lon, Double radius, List<String> categories, Integer limit) {
        URI radiusUri = buildUri("/radius", List.of(
            new BasicNameValuePair("apikey", API_KEY),
            new BasicNameValuePair("radius", radius.toString()),
            new BasicNameValuePair("lat", lat.toString()),
            new BasicNameValuePair("lon", lon.toString()),
            new BasicNameValuePair("kinds", String.join(",", categories)),
            new BasicNameValuePair("limit", limit.toString())
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

}
