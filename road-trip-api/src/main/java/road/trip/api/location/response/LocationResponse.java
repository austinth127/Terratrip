package road.trip.api.location.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import road.trip.api.location.LocationService;
import road.trip.clients.geoapify.response.Properties;

import road.trip.persistence.models.Location;
import road.trip.persistence.models.User;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Data @Builder @AllArgsConstructor
public class LocationResponse implements Comparable<LocationResponse> {
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
        osmId = location.getOsmId();
        otmId = location.getOtmId();
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
            userRating = locationService.getRatingByLocationAndUser(location, user);
        }
        phoneContact = location.getPhoneContact();
        website = location.getWebsite();
        address = location.getAddress();
        geoapifyId = location.getGeoapifyId();
        osmId = location.getOsmId();
        otmId = location.getOtmId();

        if (location.getCategories() != null) {
            categories = Arrays.stream(location.getCategories().split(",")).toList();
        }
        else {
            categories = null;
        }
    }

    public LocationResponse(Properties properties) {
        address = properties.getAddress();
        name = properties.getName();
        geoapifyId = properties.getPlaceId();
        try {
            osmId = properties.getDatasource().getRaw().getOsmId();
        } catch (NullPointerException e) {
            osmId = null;
        }
        otmId = null;
        center = new Double[]{properties.getCoordX(), properties.getCoordY()};
        categories = properties.getCategories();
        phoneContact = properties.getContact() == null ? null : properties.getContact().getPhone();
        website = properties.getWebsite();
        description = properties.getDescription();
        rating = null;
        userRating = null;
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
    private String image;
    private String address;
    private Double rating;
    private Double userRating;
    private List<String> categories;
    @JsonProperty("osm_id")
    private Long osmId;
    @JsonProperty("otm_id")
    private String otmId;
    @JsonProperty("geoapify_id")
    private String geoapifyId;

    @Override
    public int compareTo(LocationResponse o) {
        // TODO
        if (otmId != null && o.otmId == null) {
            return 1;
        }
        if (otmId == null && o.otmId != null) {
            return 0;
        }
        return 0;
    }
}
