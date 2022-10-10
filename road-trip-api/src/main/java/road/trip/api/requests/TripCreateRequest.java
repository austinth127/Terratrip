package road.trip.api.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class TripCreateRequest {

    @NotNull
    private String name;

    @NotNull @JsonProperty("advLevel")
    private String adventureLevel;

    @NotNull @JsonProperty("duration")
    private int driveDuration;
    @NotNull
    private double distance;

    @NotNull   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotNull   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull
    private LocationRequest start;
    @NotNull
    private LocationRequest end;
}
