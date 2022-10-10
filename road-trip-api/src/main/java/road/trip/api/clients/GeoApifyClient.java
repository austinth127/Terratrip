package road.trip.api.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class GeoApifyClient {


    private static final String BASE_URL = "https://api.geoapify.com";

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

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

    //TODO: Return a list not a Stop, also test
    public Stop getStopByName(String name){
        URI uri = buildUri("/v2/places?name=" + name + "&limit=20");
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(uri)
            .GET()
            .build();
        Request<Stop> request = new Request<>(httpRequest, Stop.class);

        return request.execute().body();
    }

    //TODO: Return a list not a Stop, also test
    public Stop getStopsByCoords(long lon, long lat, long radius){
        URI uri = buildUri("/v2/places?filter=circle:" + lon + "," + lat + "," + radius + "&limit=20");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        Request<Stop> request = new Request<>(httpRequest, Stop.class);

        return request.execute().body();
    }

    //TODO: Return a list not a Stop, also test
    public Stop getRecommendedStops(long lon1, long lat1, long lon2, long lat2){
        URI uri = buildUri("/v2/places?filter=rect:" + lon1 + "," + lat1 + "," + lon2 + "," + lat2 + "," + "&limit=20");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        Request<Stop> request = new Request<>(httpRequest, Stop.class);

        return request.execute().body();
    }

    class Request<T> {
        private final HttpRequest httpRequest;
        private final Class<T> clazz;

        Request(HttpRequest httpRequest, Class<T> clazz) {
            this.httpRequest = httpRequest;
            this.clazz = clazz;
        }

        HttpResponse<T> execute() {
            try {
                HttpResponse<String> httpResponse = client.send(httpRequest, BodyHandlers.ofString());
                return new HttpResponse<T>() {
                    @Override
                    public int statusCode() {
                        return httpResponse.statusCode();
                    }

                    @Override
                    public HttpRequest request() {
                        return httpResponse.request();
                    }

                    @Override
                    public Optional<HttpResponse<T>> previousResponse() {
                        return Optional.empty();
                    }

                    @Override
                    public HttpHeaders headers() {
                        return httpResponse.headers();
                    }

                    @Override
                    public T body() {
                        try {
                            return mapper.readValue(httpResponse.body(), clazz);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public Optional<SSLSession> sslSession() {
                        return httpResponse.sslSession();
                    }

                    @Override
                    public URI uri() {
                        return httpResponse.uri();
                    }

                    @Override
                    public HttpClient.Version version() {
                        return httpResponse.version();
                    }
                };
            } catch (IOException | InterruptedException e) {
                log.error(e);
                return null;
            }
        }
    }
}
