package road.trip.api.location;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.location.response.LocationResponse;
import road.trip.persistence.daos.LocationRepository;
import road.trip.persistence.daos.StopRepository;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocationService {

    private final LocationRepository locationRepository;
    private final StopRepository stopRepository;

    public Stop createStop(Trip trip, Location location, int order){

        Stop stop = Stop.builder()
                .location(location)
                .order(order)
                .trip(trip)
                .build();

        return stopRepository.save(stop);
    }

    public Location createLocation(LocationRequest locationRequest) {
        Location l = findLocation(locationRequest);
        if(l == null) {
            System.out.println("trip Created: " + locationRequest.getName());
            Location location = Location.builder()
                .name(locationRequest.getName())
                .description(locationRequest.getDescription())
                .rating(locationRequest.getRating())
                .coordX(locationRequest.getCoordX())
                .coordY(locationRequest.getCoordY())
                .build();

            return locationRepository.save(location);
        }

        return l;
    }

    public Optional<Location> getLocationById(Long locId) {
        return locationRepository.findById(locId);
    }

    public Location findLocation(LocationRequest request) {
        List<Location> locs = locationRepository.findByNameAndCoordXAndCoordY(request.getName(), request.getCoordX(), request.getCoordY());
        if(locs.size() == 1) {
            return locs.get(0);
        }
        else if (locs.size() > 1) {
            log.error("Duplicate Data in Database");
        }
        else {
            log.info("No Stop found");
        }
        return null;
    }

    public List<Location> getLocationsForTrip(Long tripId) {
        List<Stop> stops = stopRepository.findByTrip_Id(tripId).stream()
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
    public List<LocationResponse> getRecommendedStops(Long tripId, Double range) {
        // TODO
        return null;
    }
}
