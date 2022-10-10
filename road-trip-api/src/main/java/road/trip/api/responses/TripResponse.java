package road.trip.api.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;
import road.trip.persistence.models.Location;

import java.time.LocalDate;
import java.util.List;

@Data
public class TripResponse {

    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NonNull
    private int duration;
    @NonNull
    private String advLevel;
    private List<Location> stops;
}
