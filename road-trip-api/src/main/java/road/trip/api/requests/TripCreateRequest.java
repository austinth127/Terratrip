package road.trip.api.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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
    @NotNull   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate startDate;
    @NotNull   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate endDate;
//
//    @NotNull
//    private Location start;
//    @NotNull
//    private Location end;

}
