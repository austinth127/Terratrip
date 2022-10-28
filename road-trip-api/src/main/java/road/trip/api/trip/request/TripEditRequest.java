package road.trip.api.trip.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import road.trip.api.location.request.LocationRequest;
import road.trip.persistence.models.AdventureLevel;

import java.time.LocalDate;
import java.util.List;

@Data
public class TripEditRequest {

    private String name;

    @JsonProperty("advLevel")
    private AdventureLevel adventureLevel;

    @JsonProperty("duration")
    private Double driveDuration;

    private Double distance;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private LocationRequest start;
    private LocationRequest end;

    private List<LocationRequest> stops;
}
