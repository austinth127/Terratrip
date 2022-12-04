package road.trip.api.location.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class RecommendationResponse {
    List<LocationResponse> locations;
    Boolean isDone;
}
