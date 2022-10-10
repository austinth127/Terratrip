package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import road.trip.api.requests.LocationRequest;
import road.trip.api.responses.LocationsResponse;
import road.trip.persistence.daos.LocationRepository;
import road.trip.persistence.models.Location;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocationService {

    private final LocationRepository locationRepository;

    public Location createStop(LocationRequest locationRequest) {

        Location location = Location.builder()
            .name(locationRequest.getName())
            .description(locationRequest.getDescription())
            .rating(locationRequest.getRating())
            .build();

        return locationRepository.save(location);
    }

    /**
     * Gets a recommended list of stops for the given trip. Searches in the given range around the
     * route, and recommends outdoors activities and sleep locations.
     */
    public ResponseEntity<LocationsResponse> getRecommendedStops(Long tripId, Double range) {
        // TODO
        return null;
    }
}
