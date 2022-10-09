package road.trip.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.requests.StopRequest;
import road.trip.api.requests.TripRequest;
import road.trip.api.services.StopService;
import road.trip.api.services.TripService;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

@Log4j2
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TripController {

    final TripService tripService;

    @PostMapping("/create-trip")
    public ResponseEntity<Trip> saveStop(@RequestBody TripRequest tripRequest) {
        return ResponseEntity.ok(tripService.createTrip(tripRequest));
    }

}
