package road.trip.api.requests;

import lombok.Builder;
import lombok.Data;
import road.trip.persistence.models.Stop;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class TripRequest {

    @NotNull
    private String name;
    @NotNull
    private int adventureLevel;
    @NotNull
    private int duration;
    @NotNull
    private double distance;
    @NotNull
    private LocalDateTime startDate;
//
//    @NotNull
//    private Stop start;
//    @NotNull
//    private Stop end;

}
