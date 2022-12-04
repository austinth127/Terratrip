package road.trip.clients.geoapify.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Properties {
    String name;
    @JsonProperty("formatted")
    String address;
    @JsonProperty("place_id")
    String placeId;
    @JsonProperty("lon")
    Double coordX;
    @JsonProperty("lat")
    Double coordY;
    Contact contact;
    String website;
    String description;
    List<String> categories;
    Datasource datasource;

    @Data
    public static class Contact {
        String phone;
        String email;
    }

    @Data
    public static class Datasource {
        Raw raw;
        @Data
        public static class Raw {
            @JsonProperty("osm_id")
            Long osmId;
            @JsonProperty("wikidata")
            String wikidataId;
            String image;
        }
    }
}
