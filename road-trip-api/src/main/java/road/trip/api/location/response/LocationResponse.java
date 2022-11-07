package road.trip.api.location.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import road.trip.persistence.models.Location;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Data
public class LocationResponse {
    public LocationResponse(Location start) {
        id = start.getId();
        name = start.getName();
        center = new Double[]{start.getCoordX(), start.getCoordY()};
        description = start.getDescription();
        rating = start.getRating();
        phoneContact = start.getPhoneContact();
        website = start.getWebsite();
        address = start.getAddress();
        categories = Arrays.stream(start.getCategories().split(",")).toList();
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
}
