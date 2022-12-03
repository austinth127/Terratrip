package road.trip.api.location;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.location.request.LocationRatingRequest;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.location.request.RecommendationRequest;
import road.trip.api.location.response.LocationResponse;
import road.trip.api.location.response.RecommendationResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.LocationRatingRepository;
import road.trip.persistence.daos.LocationRepository;
import road.trip.persistence.models.Location;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/location")
public class LocationController {

    final LocationService locationService;
    final LocationRatingRepository locationRatingRepository;
    final UserService userService;
    final LocationRepository locationRepository;
    final RecommendationService recommendationService;

    @PostMapping("/recommend")
    public ResponseEntity<?> startRecommendedLocationRequests(@RequestBody RecommendationRequest recommendationRequest) {
        log.debug(recommendationRequest);
        recommendationService.startRecommendationRequests(recommendationRequest.getTripId(), recommendationRequest.getRange(), recommendationRequest.getCategories(), recommendationRequest.getRoute(), recommendationRequest.getLimit());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recommend")
    public ResponseEntity<RecommendationResponse> getRecommendedLocations(Integer limit) {
        return ResponseEntity.ok(recommendationService.getRecommendedLocations(limit));
    }

    @PostMapping("/{location_id}")
    public ResponseEntity<?> rateLocation(@PathVariable("location_id") Long id, @RequestBody LocationRatingRequest ratingRequest){
        locationService.addLocationRating(id, ratingRequest.getRating());
        return ResponseEntity.ok().build();
    }
}
