package road.trip.api.location.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import road.trip.persistence.models.Location;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class LocationRequest {

    @NotNull @JsonProperty("place_name")
    private String name;
    @NotNull @JsonProperty("lng")
    private double coordX;
    @NotNull @JsonProperty("lat")
    private double coordY;

    private String description;
    private Double rating;

    @JsonProperty("geoapify_id")
    private String geoapifyId;
    @JsonProperty("otm_id")
    private String otmId;
    @JsonProperty("osm_id")
    private Long osmId;
    @JsonProperty("wikidata_id")
    private String wikidataId;
    private String image;

    @JsonProperty("phone_contact")
    private String phoneContact;
    private String website;
    private String address;
    private List<String> categories;

    public Location buildLocation() {
        return Location.builder()
            .categories(categories == null ? null : String.join(",", categories))
            .website(website)
            .description(description)
            .phoneContact(phoneContact)
            .coordY(coordY)
            .coordX(coordX)
            .geoapifyId(geoapifyId)
            .name(name)
            .rating(rating == null ? 0 : rating)
            .address(address)
            .osmId(osmId)
            .otmId(otmId)
            .wikidataId(wikidataId)
            .imageUrl(image)
            .build();
    }

}
