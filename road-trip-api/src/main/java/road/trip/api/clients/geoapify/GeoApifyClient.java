package road.trip.api.clients.geoapify;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.persistence.models.Location;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeoApifyClient {

    private static final String BASE_URL = "https://api.geoapify.com";

    private final HttpClient client = HttpClient.newHttpClient();
    private final LocationMapper locationMapper;

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

        return httpResponse.body();
    }

    //TODO: test
    public List<Location> getStopByName(String name){
        URI uri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("name", name),
            new BasicNameValuePair("limit", "20")));
        try {
            String jsonBody = doGet(uri);
            return locationMapper.getStopsFromJSON(jsonBody);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    //TODO: test
    public List<Location> getStopsByCoords(long lon, long lat, long radius){
        URI uri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("filter", "circle:" + lon + "," + lat + "," + radius),
            new BasicNameValuePair("limit", "20")));
        try {
            String jsonBody = doGet(uri);
            return locationMapper.getStopsFromJSON(jsonBody);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    //TODO: test
    public List<Location> getRecommendedStops(long lon1, long lat1, long lon2, long lat2){
        URI uri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("filter", "rect:" + lon1 + "," + lat1 + "," + lon2 + "," + lat2 + ","),
            new BasicNameValuePair("limit", "20")));
        try {
            String jsonBody = doGet(uri);
            return locationMapper.getStopsFromJSON(jsonBody);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
