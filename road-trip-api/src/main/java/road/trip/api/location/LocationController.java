package road.trip.api.location;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.location.response.LocationResponse;
import road.trip.persistence.models.Location;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/stop")
public class LocationController {

    final LocationService locationService;

    @Deprecated
    @PostMapping("/create-stop")
    public ResponseEntity<Location> saveStop(@RequestBody LocationRequest locationRequest) {
        return ResponseEntity.ok(locationService.createLocation(locationRequest));
    }

    @GetMapping
    public ResponseEntity<List<LocationResponse>> getRecommendedStops(Long tripId, Double range) {
        return ResponseEntity.ok(locationService.getRecommendedStops(tripId, range));
    }
}
