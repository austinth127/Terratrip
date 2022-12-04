package road.trip.clients.wikidata;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import road.trip.clients.wikidata.response.ImageClaimsResponse;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static road.trip.util.UtilityFunctions.doGet;

@Service
@Log4j2
public class WikidataClient {
    private static final String BASE_URL = "https://www.wikidata.org";
    private static final String BASE_PATH = "/w/api.php";

    private final HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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

    public String getImageUrlFromWikidataId(String wikidataId) {
        try {
            URI imageNameUri = buildUri("", List.of(
                new BasicNameValuePair("action", "wbgetclaims"),
                new BasicNameValuePair("property", "P18"),
                new BasicNameValuePair("entity", wikidataId),
                new BasicNameValuePair("format", "json")
            ));
            String jsonBody = doGet(client, imageNameUri);
            ImageClaimsResponse imageClaimsResponse = mapper.readValue(jsonBody, ImageClaimsResponse.class);

            String imageName;
            try {
                imageName = imageClaimsResponse.getClaims().getP18().get(0).getMainsnak().getDatavalue().getValue();
            } catch (NullPointerException e) {
                log.warn("Can't get image name from WikiData API, id=" + wikidataId);
                log.debug(e.getStackTrace());
                return null;
            }
            log.debug("Image found for " + wikidataId + ": " + imageName);

            String uriOne = "https://commons.wikimedia.org/w/index.php?title=Special:Redirect/file/" + imageName.replaceAll(" ", "_");
            log.debug("First uri: " + uriOne);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(uriOne))
                .GET()
                .build();
            HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String uriTwo = httpResponse.headers().firstValue("location").orElse(null);
            log.info("Real uri: " + uriTwo);

            return uriTwo;
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
