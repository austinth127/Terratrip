package road.trip.api.location;

import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import road.trip.api.category.CategoryService;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.user.UserService;
import road.trip.clients.geoapify.GeoApifyClient;
import road.trip.persistence.daos.*;
import road.trip.persistence.models.*;

import java.util.*;

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

    public void addLocationRating(Long locationID, Double rating){
        //rating exists for that user.
        Location location = locationRepository.findById(locationID).orElseThrow();
        LocationRating prevRating = locationRatingRepository.findByUserAndLocation(userService.user(), location)
            .orElse(null);
        if(Objects.isNull(prevRating)){
            locationRatingRepository.save(LocationRating.builder()
                .location(locationRepository.findById(locationID).get()).rating(rating).user(userService.user())
                .build());
        }
        else{
            prevRating.setRating(rating);
            locationRatingRepository.save(prevRating);
        }

    }

    public Double getRatingByLocationAndUser(Location location, User user) {
        Optional<LocationRating> ret = locationRatingRepository.findByUserAndLocation(user, location);
        return ret.map(LocationRating::getRating).orElse(null);
    }

    public Double getAverageRating(Location location) {
        Double ratingsValue = 0.0;
        int count = 0;
        List<LocationRating> ratings = locationRatingRepository.findAllByLocation(location);
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

        if(location.getCategories() == null) {
            return null;
        }

        String[] categories = location.getCategories().split(",");

        for(int i = 0; i < categories.length; i++) {
            List<Category> c = categoryRepository.findAllByUiName(categories[i]);
            if(c.size() > 0) {
                if(advLevel == null || advLevel.ordinal() > c.get(0).getAdventureLevel().ordinal()) {
                    advLevel = c.get(0).getAdventureLevel();
                }
            }
        }
        return advLevel;
    }
}
