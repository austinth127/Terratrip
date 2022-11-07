package road.trip.api.location;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.location.request.LocationRatingRequest;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.location.request.RecommendRequest;
import road.trip.api.location.response.LocationResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.LocationRatingRepository;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.LocationRating;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/location")
public class LocationController {

    final LocationService locationService;
    final LocationRatingRepository locationRatingRepository;
    final UserService userService;

    @Deprecated
    @PostMapping("/create-stop")
    public ResponseEntity<Location> saveStop(@RequestBody LocationRequest locationRequest) {
        return ResponseEntity.ok(locationService.createLocation(locationRequest));
    }

    @PostMapping("/recommend")
    public ResponseEntity<List<LocationResponse>> getRecommendedLocations(@RequestBody RecommendRequest recommendRequest) {
        log.info(recommendRequest);
        log.info(recommendRequest.getRoute());
        return ResponseEntity.ok(locationService.getRecommendedLocations(recommendRequest.getTripId(), recommendRequest.getRange(), recommendRequest.getCategories(), recommendRequest.getRoute()));

    }

    @PostMapping("/{location_id}")
    public ResponseEntity<?> rateLocation(@PathVariable("location_id") Long id, @RequestBody LocationRatingRequest ratingRequest){
        locationService.addLocationRating(id, ratingRequest.getRating());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{location_id}")
    public ResponseEntity<?> getLocationRating(@PathVariable("location_id") Long id){
        return ResponseEntity.ok(locationService.getRatingByIDAndUser(id,userService.user()));
    }
}
