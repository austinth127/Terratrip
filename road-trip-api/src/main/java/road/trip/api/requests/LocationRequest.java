package road.trip.api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class LocationRequest {
    @NotNull @JsonProperty("place_name")
    private String name;
    @NotNull @JsonProperty("center")
    private void unpackCenter(Double[] center) {
        this.coordX = center[0];
        this.coordY = center[1];
    }

    private double coordX;
    private double coordY;

    private String description;
    private int rating;

//    @NotNull
//    private String type;
//    @NotNull
//    private String geoType;
}
