package road.trip.clients.geoapify;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import road.trip.persistence.models.Location;

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

    @Value("${geoapify-key}")
    private String API_KEY;

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
    public List<Location> getStopByName(String name, int limit, double lon1, double lat1, double lon2, double lat2){
        URI uri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("name", name),
            new BasicNameValuePair("limit", "" + limit),
            new BasicNameValuePair("filter", "rect:" + lon1 + "," + lat1 + "," + lon2 + "," + lat2),
            new BasicNameValuePair("apiKey", "a9b12a2a2ae0491cb7874bbf0fab7115")));
        System.out.println(uri.toString());
        try {
            String jsonBody = doGet(uri);
            System.out.println(jsonBody);
            return locationMapper.getStopsFromJSON(jsonBody);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    //TODO: test
    public List<Location> getStopsByCoords(double lon, double lat, double radius){
        URI uri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("filter", "circle:" + lon + "," + lat + "," + radius),
            new BasicNameValuePair("limit", "20"),
            new BasicNameValuePair("apiKey", "a9b12a2a2ae0491cb7874bbf0fab7115")));
        try {
            String jsonBody = doGet(uri);
            return locationMapper.getStopsFromJSON(jsonBody);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    //https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=rect%3A10.716463143326969%2C48.755151258420966%2C10.835314015356737%2C48.680903341613316&limit=20&apiKey=a9b12a2a2ae0491cb7874bbf0fab7115
    public List<Location> getRecommendedStops(double minLon, double minLat, double maxLon, double maxLat, List<String> categories) {
        URI uri = buildUri("/v2/places", List.of(
            new BasicNameValuePair("categories", String.join(",", categories)),
            new BasicNameValuePair("filter", "rect:" + minLon + "," + minLat + "," + maxLon + "," + maxLat),
            new BasicNameValuePair("limit", "20"),
            new BasicNameValuePair("apiKey", API_KEY)));
        try {
            String jsonBody = doGet(uri);
            return locationMapper.getStopsFromJSON(jsonBody);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}