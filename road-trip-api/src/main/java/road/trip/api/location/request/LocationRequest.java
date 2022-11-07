package road.trip.api.location.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import road.trip.persistence.models.Location;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class LocationRequest {

    @NotNull @JsonProperty("place_name")
    private String name;
    @NotNull @JsonProperty("lng")
    private double coordX;
    @NotNull @JsonProperty("lat")
    private double coordY;

    private String description;
    private Integer rating;

    @JsonProperty("geoapify_id")
    private String geoapifyId;

    private String phoneContact;
    private String website;
    private String address;
    private List<String> categories;

    public Location buildLocation() {
        return Location.builder()
            .categories(String.join(",", categories))
            .website(website)
            .description(description)
            .phoneContact(phoneContact)
            .coordY(coordY)
            .coordX(coordX)
            .geoapifyId(geoapifyId)
            .name(name)
            .rating(rating)
            .address(address)
            .build();
    }

}
