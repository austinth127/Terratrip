package road.trip.api.requests;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class TripCreateRequest {

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
//    private Location start;
//    @NotNull
//    private Location end;

}
