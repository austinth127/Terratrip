package road.trip.api.location;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.location.response.LocationResponse;
import road.trip.clients.geoapify.GeoApifyClient;
import road.trip.persistence.daos.LocationRepository;
import road.trip.persistence.daos.StopRepository;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

import java.util.*;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocationService {

    private final LocationRepository locationRepository;
    private final StopRepository stopRepository;

    private final TripRepository tripRepository;

    private final GeoApifyClient geoApifyClient;

    public Stop createStop(Trip trip, Location location, int order){

        Stop stop = Stop.builder()
                .location(location)
                .order(order)
                .trip(trip)
                .build();

        return stopRepository.save(stop);
    }

    public Location createLocation(LocationRequest locationRequest) {
        Optional<Location> l = locationRepository.findById(locationRequest.getId());
        if(l.isEmpty()) {
            Location location = Location.builder()
                .name(locationRequest.getName())
                .description(locationRequest.getDescription())
                .rating(locationRequest.getRating())
                .coordX(locationRequest.getCoordX())
                .coordY(locationRequest.getCoordY())
                .build();

            return locationRepository.save(location);
        }

        return l.get();
    }

    public void updateOrder(Long tripId, Long locId,  int order, int newOrder) {
        Optional<Stop> s = stopRepository.findByTrip_IdAndLocation_IdAndOrder(tripId, locId, order);
        if(s.isPresent()) {
            s.get().setOrder(newOrder);
            stopRepository.save(s.get());
        }

    }

    public Optional<Location> getLocationById(Long locId) {
        return locationRepository.findById(locId);
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

    //TODO: Limit number of stops per request dynamically (based on route points)
    /**
     * Gets a recommended list of stops for the given trip. Searches in the given range around the
     * route, and recommends outdoors activities and sleep locations.
     */
    public List<LocationResponse> getRecommendedLocations(Long tripId, Double radius, List<String> categories, List<List<Double>> route) {
        log.info(tripId + " " + radius + " " + categories + " " + route);
        Optional<Trip> optTrip = tripRepository.findById(tripId);
        List<LocationResponse> locationResponses = new ArrayList<>();
        if(optTrip.isPresent()) {
            for(List<Double> point : route){
                locationResponses.addAll(geoApifyClient.getRecommendedLocations(point.get(0), point.get(1), radius, categories, 5));
            }
        } else{
            // TODO: add log message
            System.out.println("Error: no Trip found");
        }
        return locationResponses;
    }
}
