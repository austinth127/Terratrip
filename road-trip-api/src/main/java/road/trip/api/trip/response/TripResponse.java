package road.trip.api.trip.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import road.trip.api.location.response.LocationResponse;
import road.trip.api.trip.response.ReducedTripResponse;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
public class TripResponse extends ReducedTripResponse {
    public TripResponse(Trip t) {
        distance = t.getDistance();
        duration = t.getDriveDuration();
        advLevel = t.getAdventureLevel().toString();
        startDate = t.getStartDate();
        endDate = t.getEndDate();
        id = t.getId();
        start = t.getStart() == null ? null : new LocationResponse(t.getStart());
        end = t.getEnd() == null ? null : new LocationResponse(t.getEnd());
        stops = t.getStops().stream()
            .map(Stop::getLocation)
            .map(LocationResponse::new)
            .collect(Collectors.toList());
        rating = t.getRating();
    }

    @NonNull
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private String advLevel;

    private Double duration;

    private Double distance;

    private Double rating;

    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NonNull
    private LocationResponse start;
    @NonNull
    private LocationResponse end;

    private List<LocationResponse> stops;
}
