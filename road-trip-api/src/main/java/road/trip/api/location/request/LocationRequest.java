package road.trip.api.location.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

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
    private int rating;

}
