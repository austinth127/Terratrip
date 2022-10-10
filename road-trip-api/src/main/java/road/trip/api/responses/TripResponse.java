package road.trip.api.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;
import road.trip.persistence.models.Location;

import java.time.LocalDate;
import java.util.List;

@Data
public class TripResponse extends ReducedTripResponse {
    private List<Location> stops;
}
