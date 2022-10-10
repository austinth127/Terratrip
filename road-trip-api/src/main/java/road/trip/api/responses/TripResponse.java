package road.trip.api.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.bind.annotation.ResponseBody;
import road.trip.persistence.models.Location;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class TripResponse {

    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate startDate;
    @NonNull
    private int duration;
    @NonNull
    private int advLevel;
    private List<Location> stops;
}
