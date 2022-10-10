package road.trip.api.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class TripEditRequest {

    private String name;

    @JsonProperty("advLevel")
    private String adventureLevel;

    @JsonProperty("duration")
    private int driveDuration;

    private double distance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private LocationRequest start;
    private LocationRequest end;

    private List<LocationRequest> stops;
}
