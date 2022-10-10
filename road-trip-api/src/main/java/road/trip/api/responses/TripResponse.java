package road.trip.api.responses;

import lombok.Data;
import lombok.NonNull;
import org.springframework.web.bind.annotation.ResponseBody;
import road.trip.persistence.models.Location;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class TripResponse {

    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private LocalDateTime startDate;
    @NonNull
    private int duration;
    @NonNull
    private int advLevel;
    private List<Location> stops;
}
