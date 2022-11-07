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

    @Data
    static class Contact {
        String phone;
        String email;
    }
}
