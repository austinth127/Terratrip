package road.trip.api.location.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class RecommendationRequest {

    @NotNull
    @JsonProperty("tripId")
    private Long tripId;
    @NotNull @JsonProperty("range")
    private double range;
    @NotNull @JsonProperty("categories")
    private Set<String> categories;
    @NotNull @JsonProperty("route")
    private List<List<Double>> route;
    private Integer limit;

}
