package road.trip.clients.geoapify.response;

import lombok.Data;
import road.trip.api.location.response.LocationResponse;
import road.trip.persistence.models.Location;

import java.util.List;

@Data
public class Feature {
    String type;
    Properties properties;
}
