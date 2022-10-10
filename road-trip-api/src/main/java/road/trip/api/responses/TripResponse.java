package road.trip.api.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import road.trip.persistence.models.Location;
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
        start = new LocationResponse(t.getStart());
        end = new LocationResponse(t.getEnd());
        stops = t.getStops().stream().map(Stop::getLocation).collect(Collectors.toList());
    }

    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NonNull
    private double duration;
    @NotNull
    private double distance;
    @NonNull
    private String advLevel;
    @NonNull
    private LocationResponse start;
    @NonNull
    private LocationResponse end;

    private List<Location> stops;
}
