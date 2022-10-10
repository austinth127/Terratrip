package road.trip.api.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import road.trip.persistence.models.Location;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
public class TripResponse extends ReducedTripResponse {
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
