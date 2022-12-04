package road.trip.clients.wikidata.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data public class ImageClaimsResponse {
    Claims claims;

    @Data public static class Claims {
        @JsonProperty("P18")
        List<P18> p18;

        @Data public static class P18 {
            MainSnak mainsnak;

            @Data public static class MainSnak {
                DataValue datavalue;

                @Data public static class DataValue {
                    String value;
                }
            }
        }
    }
}
