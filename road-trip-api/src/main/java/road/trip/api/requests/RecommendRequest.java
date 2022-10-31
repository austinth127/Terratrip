package road.trip.api.location.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class RecommendRequest {

    @NotNull
    @JsonProperty("tripId")
    private Long tripId;
    @NotNull @JsonProperty("range")
    private double range;
    @NotNull @JsonProperty("categories")
    private List<String> categories;
    @NotNull @JsonProperty("route")
    private List<List<Double>> route;

}
