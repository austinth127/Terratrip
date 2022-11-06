package road.trip.clients.geoapify.response;

import lombok.Data;

import java.util.List;

@Data
public class FeatureCollection {
    String type;
    List<Feature> features;
}
