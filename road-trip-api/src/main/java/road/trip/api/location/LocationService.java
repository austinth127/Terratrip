package road.trip.api.location;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import road.trip.api.category.CategoryService;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.location.response.LocationResponse;
import road.trip.api.user.UserService;
import road.trip.clients.geoapify.GeoApifyClient;
import road.trip.persistence.daos.*;
import road.trip.persistence.models.*;
import road.trip.util.exceptions.NotFoundException;

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
    private final LocationRatingRepository locationRatingRepository;
    private final UserService userService;

    private final CategoryRepository categoryRepository;

    public Stop createStop(Trip trip, Location location, int order) {


        Stop stop = Stop.builder()
                .location(location)
                .order(order)
                .trip(trip)
                .build();

        return stopRepository.save(stop);
    }

    public Location createLocation(LocationRequest locationRequest) {
        Location l = findLocation(locationRequest);
        if (l == null) {
            Location location = locationRequest.buildLocation();
            return locationRepository.save(location);
        }

        return l;
    }

    public Location findLocation(LocationRequest request) {
        List<Location> locs = locationRepository.findByNameAndCoordXAndCoordY(request.getName(), request.getCoordX(), request.getCoordY());
        if(locs.size() == 1) {
            return locs.get(0);
        }
        else if (locs.size() > 1) {
            log.error("Duplicate Data in Database");
            return locs.get(0);
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
        Integer limit = 1;

        Optional<Trip> optTrip = tripRepository.findById(tripId);
        Set<Location> locations = new HashSet<>();
        List<String> categories = categoryService.getCategoriesByApi(frontendCategories, PlacesAPI.GEOAPIFY);

        log.debug(tripId + " " + radius + " " + categories + " " + route);
        log.info("Get Recommended Locations");
        if(optTrip.isPresent()) {
            if(categories.isEmpty()){   
                categories = categoryService.getRecommendedCategories(optTrip.get());
            }
            for(List<Double> point : route){
                locations.addAll(geoApifyClient.getRecommendedLocations(point.get(0), point.get(1), radius, categories, limit));
            }
        } else{
            log.error("Error: no Trip found");
        }

        return locations.stream()
            .map(location -> {
                // Set the categories for the given location
                location.setCategories(String.join(",", Arrays.stream(location.getCategories().split(","))
                    .map(categoryService::getCategoryFromCategoryApiName)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(Category::getName)
                    .toList()));
                return location;
            }).map(l -> new LocationResponse(l, userService.user(), this))     // Location -> LocationResponse
            .collect(Collectors.toList());  // stream -> List
    }

    public void addLocationRating(Long locationID, Double rating){
        //rating exists for that user.
        if (!locationRepository.existsById(locationID)){
            throw new NotFoundException();
        }
        LocationRating prevRating = locationRatingRepository.findAllByRatingUserAndRatedLocation(userService.user(),locationRepository.findById(locationID));
        if(Objects.isNull(prevRating)){
            locationRatingRepository.save(LocationRating.builder()
                .ratedLocation(locationRepository.findById(locationID).get()).rating(rating).ratingUser(userService.user())
                .build());
        }
        else{
            prevRating.setRating(rating);
            locationRatingRepository.save(prevRating);
        }

    }

    public Double getRatingByIDAndUser(Long id, User u ) {
        LocationRating ret = locationRatingRepository.findAllByRatingUserAndRatedLocation(u,locationRepository.findById(id));
        if(Objects.isNull(ret)){
//            throw new NotFoundException();
            return null;
        }
        return ret.getRating();
    }

    public Double getAverageRating(Location location) {
        Double ratingsValue = 0.0;
        int count = 0;
        List<LocationRating> ratings = locationRatingRepository.findAllByRatedLocation(location);
        for(int i = 0; i < ratings.size();i++){
            ratingsValue+= ratings.get(i).getRating();
            count++;
        }
        if (count == 0) {
            return null;
        }
        return ratingsValue/count;
    }

    public AdventureLevel getAdventureLevel(Location location) {
        AdventureLevel advLevel = null;

        String[] categories = location.getCategories().split(",");

        for(int i = 0; i < categories.length; i++) {
            Optional<Category> c = categoryRepository.findByName(categories[i]);
            if(c.isPresent()) {
                if(advLevel == null || advLevel.ordinal() > c.get().getAdventureLevel().ordinal()) {
                    advLevel = c.get().getAdventureLevel();
                }
            }
        }
        return advLevel;
    }
}
