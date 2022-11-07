package road.trip.api.location;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import road.trip.api.category.CategoryService;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.location.response.LocationResponse;
import road.trip.clients.geoapify.GeoApifyClient;
import road.trip.persistence.daos.LocationRepository;
import road.trip.persistence.daos.StopRepository;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.PlacesAPI;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LocationService {

    private final LocationRepository locationRepository;
    private final StopRepository stopRepository;

    private final CategoryService categoryService;
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

    //TODO: Limit number of stops per request dynamically (based on route points)
    /**
     * Gets a recommended list of stops for the given trip. Searches in the given range around the
     * route, and recommends outdoors activities and sleep locations.
     */
    public List<LocationResponse> getRecommendedLocations(Long tripId, Double radius, List<String> frontendCategories, List<List<Double>> route) {
        //The approximate number of total results the function returns
        final Integer desiredResultSize = 30;
        //The limit per request to meet the desired result size
//        Integer limit = desiredResultSize / route.size();
        Integer limit = 1;
//        if(limit == 0){ limit = 1; } //Special case where there are more points along the route than the desired result size

        Optional<Trip> optTrip = tripRepository.findById(tripId);
        List<LocationResponse> locationResponses = new ArrayList<>();
        List<String> categories = categoryService.getCategoriesByApi(frontendCategories, PlacesAPI.GEOAPIFY);

        log.info(tripId + " " + radius + " " + categories + " " + route);
        if(optTrip.isPresent()) {
            if(categories.isEmpty()){
                categories = categoryService.getRecommendedCategories(optTrip.get());
            }
            for(List<Double> point : route){
                locationResponses.addAll(geoApifyClient.getRecommendedLocations(point.get(0), point.get(1), radius, categories, limit).stream()
                    .map(LocationResponse::new)
                    .collect(Collectors.toList()));
            }
        } else{
            // TODO: add log message
            System.out.println("Error: no Trip found");
        }
        return locationResponses;
    }
}
