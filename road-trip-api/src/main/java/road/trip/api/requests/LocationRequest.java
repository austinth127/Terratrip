package road.trip.api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class LocationRequest {

    private Long id;

    @NotNull @JsonProperty("place_name")
    private String name;
    @NotNull @JsonProperty("lng")
    private double coordX;
    @NotNull @JsonProperty("lat")
    private double coordY;

    private String description;
    private int rating;

}
