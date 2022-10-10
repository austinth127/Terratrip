package road.trip.api.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import road.trip.persistence.models.Trip;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReducedTripResponse {
    public ReducedTripResponse(Trip t){
        id = t.getId();
        name = t.getName();
        startDate = t.getStartDate();
        endDate = t.getEndDate();
        duration = t.getDriveDuration();
        distance = t.getDistance();
        advLevel = t.getAdventureLevel().toString();
        start = new LocationResponse(t.getStart());
        end = new LocationResponse(t.getEnd());
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
}