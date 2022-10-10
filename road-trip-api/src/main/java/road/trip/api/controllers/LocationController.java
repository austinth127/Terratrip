package road.trip.api.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.requests.LocationRequest;
import road.trip.api.responses.LocationsResponse;
import road.trip.api.services.LocationService;
import road.trip.persistence.models.Location;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/stop")
public class LocationController {

    final LocationService locationService;

    @Deprecated
    @PostMapping("/create-stop")
    public ResponseEntity<Location> saveStop(@RequestBody LocationRequest locationRequest) {
        return ResponseEntity.ok(locationService.createStop(locationRequest));
    }

    @GetMapping
    public ResponseEntity<LocationsResponse> getRecommendedStops(Long tripId, Double range) {
        // TODO
        return null;
    }
}
