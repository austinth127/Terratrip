package road.trip.api.trip.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import road.trip.api.location.response.LocationResponse;
import road.trip.persistence.models.Trip;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReducedTripResponse {
    public ReducedTripResponse(Trip t) {
        distance = t.getDistance();
        duration = t.getDriveDuration();
        advLevel = t.getAdventureLevel().toString();
        startDate = t.getStartDate();
        endDate = t.getEndDate();
        id = t.getId();
        start = new LocationResponse(t.getStart());
        end = new LocationResponse(t.getEnd());
        rating = t.getRating();
    }

    @NonNull
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private String advLevel;

    @NonNull
    private double duration;
    @NotNull
    private double distance;

    private double rating;

    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NonNull
    private LocationResponse start;
    @NonNull
    private LocationResponse end;
}
