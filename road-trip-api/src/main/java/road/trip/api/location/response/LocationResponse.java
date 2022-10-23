package road.trip.api.location.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import road.trip.persistence.models.Location;

import javax.validation.constraints.NotNull;


@Data
public class LocationResponse {
    public LocationResponse(Location start) {
        id = start.getId();
        name = start.getName();
        center = new Double[]{start.getCoordX(), start.getCoordY()};
        description = start.getDescription();
        rating = start.getRating();
    }

    @NotNull
    private Long id;

    @NotNull @JsonProperty("place_name")
    private String name;

    @NotNull
    private Double[] center;

    private String description;
    private int rating;
}
