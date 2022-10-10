package road.trip.api.services;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import road.trip.api.requests.LocationRequest;
import road.trip.api.responses.LocationsResponse;
import road.trip.persistence.daos.LocationRepository;
import road.trip.persistence.daos.StopRepository;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.Stop;

import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocationService {

    private final LocationRepository locationRepository;
    private final StopRepository stopRepository;

    public Location createStop(LocationRequest locationRequest) {

        Location location = Location.builder()
            .name(locationRequest.getName())
            .description(locationRequest.getDescription())
            .rating(locationRequest.getRating())
            .build();

        return locationRepository.save(location);
    }

    public List<Location> getLocationsForTrip(Long tripId) {
        List<Stop> stops = stopRepository.findByTripId(tripId).stream()
            .sorted(Comparator.comparingInt(Stop::getOrder)).toList();

        List<Location> locs = new ArrayList<>();

        for (int i = 0; i < stops.size(); i++) {
            Optional<Location> optLoc = locationRepository.findById(stops.get(i).getLocation().getId());
            if(optLoc.isPresent()) {
                locs.add(optLoc.get());
            }
            else{
                // TODO: add log message
                System.out.println("Error: no Location found");
            }
        }
        return locs;
    }

    /**
     * Gets a recommended list of stops for the given trip. Searches in the given range around the
     * route, and recommends outdoors activities and sleep locations.
     */
    public LocationsResponse getRecommendedStops(Long tripId, Double range) {
        // TODO
        return null;
    }
}
