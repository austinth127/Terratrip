package road.trip.api.location.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import road.trip.persistence.models.Location;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Data
public class LocationResponse {
    public LocationResponse(Location location) {
        id = location.getId();
        name = location.getName();
        center = new Double[]{location.getCoordX(), location.getCoordY()};
        description = location.getDescription();
        rating = location.getRating();
        phoneContact = location.getPhoneContact();
        website = location.getWebsite();
        address = location.getAddress();
        categories = Arrays.stream(location.getCategories().split(",")).toList();
        geoapifyId = location.getGeoapifyId();
    }

    @NotNull
    private Long id;

    @NotNull @JsonProperty("place_name")
    private String name;

    @NotNull
    private Double[] center;

    private String description;
    private String phoneContact;
    private String website;
    private String address;
    private Integer rating;
    private List<String> categories;

    @JsonProperty("geoapify_id")
    private String geoapifyId;
}
