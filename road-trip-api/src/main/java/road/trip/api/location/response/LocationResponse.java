package road.trip.api.location.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import road.trip.api.location.LocationService;
import road.trip.clients.geoapify.response.Feature;
import road.trip.clients.geoapify.response.Properties;

import road.trip.persistence.models.Location;
import road.trip.persistence.models.User;

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
        geoapifyId = location.getGeoapifyId();
        userRating = null;

        if (location.getCategories() != null) {
            categories = Arrays.stream(location.getCategories().split(",")).toList();
        }
        else {
            categories = null;
        }
    }
    public LocationResponse(Location location, User user, LocationService locationService){
        id = location.getId();
        name = location.getName();
        center = new Double[]{location.getCoordX(), location.getCoordY()};
        description = location.getDescription();
        if (id != null) {
            rating = locationService.getAverageRating(location);

            userRating = locationService.getRatingByIDAndUser(id,user);
        }
        phoneContact = location.getPhoneContact();
        website = location.getWebsite();
        address = location.getAddress();
        geoapifyId = location.getGeoapifyId();

        if (location.getCategories() != null) {
            categories = Arrays.stream(location.getCategories().split(",")).toList();
        }
        else {
            categories = null;
        }
    }

    //TODO:Fails to account for wrong category type and problem with phone contact
    public LocationResponse(Properties properties){
        address = properties.getAddress();
        name = properties.getName();
        geoapifyId = properties.getPlaceId();
        center = new Double[]{properties.getCoordX(), properties.getCoordY()};
        //categories = properties.getCategories();
        //TODO: Why this no work?
        //phoneContact(properties.getContact() == null ? null : properties.getContact().getPhone());
        website = properties.getWebsite();
        description = properties.getDescription();
        rating = 0.0;
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
    private Double rating;
    private List<String> categories;
    private Double userRating;

    @JsonProperty("geoapify_id")
    private String geoapifyId;
}
