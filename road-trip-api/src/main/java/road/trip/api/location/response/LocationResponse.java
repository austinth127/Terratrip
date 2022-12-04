package road.trip.api.location.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import road.trip.api.location.LocationService;
import road.trip.clients.geoapify.response.Properties;

import road.trip.persistence.models.Location;
import road.trip.persistence.models.User;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

import static road.trip.util.UtilityFunctions.bestString;
import static road.trip.util.UtilityFunctions.combineLists;

@Data @Builder @AllArgsConstructor @Log4j2
public class LocationResponse implements Comparable<LocationResponse> {
    public LocationResponse(Location location) {
        id = location.getId();
        name = location.getName();
        center = new Double[]{location.getCoordX(), location.getCoordY()};
        description = location.getDescription();
        image = location.getImageUrl();
        rating = location.getRating();
        phoneContact = location.getPhoneContact();
        website = location.getWebsite();
        address = location.getAddress();
        geoapifyId = location.getGeoapifyId();
        osmId = location.getOsmId();
        otmId = location.getOtmId();
        wikidataId = location.getWikidataId();
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
        image = location.getImageUrl();
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
        wikidataId = location.getWikidataId();

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
            osmId = Math.abs(properties.getDatasource().getRaw().getOsmId());
        } catch (NullPointerException e) {
            osmId = null;
        }
        try {
            wikidataId = properties.getDatasource().getRaw().getWikidataId();
        } catch (NullPointerException e) {
            wikidataId = null;
        }
        otmId = null;
        center = new Double[]{properties.getCoordX(), properties.getCoordY()};
        categories = properties.getCategories();
        phoneContact = properties.getContact() == null ? null : properties.getContact().getPhone();
        website = properties.getWebsite();
        description = properties.getDescription();
        image = null;
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
    @JsonProperty("wikidata_id")
    private String wikidataId;

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

    public static LocationResponse combineRecommendations(LocationResponse a, LocationResponse b) {
        try {
            if (a.getCenter() != null && b.getCenter() != null) {
                String aid = a.getGeoapifyId() != null ? a.getGeoapifyId() : a.getOtmId();
                String bid = b.getGeoapifyId() != null ? b.getGeoapifyId() : b.getOtmId();
                log.info("Combining " + aid + " with " + bid);
            }
        } catch (Exception e) {
            log.error(e);
        }

        return LocationResponse.builder()
            .name(bestString(a.getName(), b.getName()))
            .center(a.getCenter())
            .description(bestString(a.getDescription(), b.getDescription()))
            .wikidataId(bestString(a.getWikidataId(), b.getWikidataId()))
            .phoneContact(bestString(a.getPhoneContact(), b.getPhoneContact()))
            .website(bestString(a.getWebsite(), b.getWebsite()))
            .image(bestString(a.getImage(), b.getImage()))
            .address(bestString(a.getAddress(), b.getAddress()))
            .rating(a.getRating() != null ? a.getRating() : b.getRating())
            .userRating(a.getUserRating() != null ? a.getUserRating() : b.getUserRating())
            .categories(combineLists(a.getCategories(), b.getCategories()))
            .otmId(bestString(a.getOtmId(), b.getOtmId()))
            .osmId(a.getOsmId() != null ? a.getOsmId() : b.getOsmId())
            .geoapifyId(bestString(a.getGeoapifyId(), b.getGeoapifyId()))
            .build();
    }
}
