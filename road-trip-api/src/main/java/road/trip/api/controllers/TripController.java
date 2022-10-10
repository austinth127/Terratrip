package road.trip.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.requests.TripCreateRequest;
import road.trip.api.requests.TripEditRequest;
import road.trip.api.responses.TripResponse;
import road.trip.api.responses.TripsResponse;
import road.trip.api.services.TripService;
import road.trip.persistence.models.Trip;

@Log4j2
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/trip")
public class TripController {

    final TripService tripService;

    @Deprecated
    @PostMapping("/create-trip")
    public ResponseEntity<Long> saveStop(@RequestBody TripCreateRequest tripCreateRequest) {
        return tripService.createTrip(tripCreateRequest);
    }

    /**
     * Gets all the trips created by the user making the request
     */
    @GetMapping
    public ResponseEntity<TripsResponse> getTrips() {
        // TODO
        return null;
    }

    /**
     * Gets a trip by id. Should only return the trip if the current user
     * owns that trip.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTrip(@PathVariable("id") Long id) {
        // TODO
        return null;
    }

    /**
     * Creates a trip and returns the id of the newly created trip
     */
    @PostMapping
    public ResponseEntity<Long> createTrip(@RequestBody TripCreateRequest request) {
        // TODO
        return null;
    }

    /**
     * Modifies the trip of the given id. Should only edit the trip
     * if it is owned by the user making the request.
     *
     * Use this request to add/delete locations as well.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> editTrip(@PathVariable("id") String id, @RequestBody TripEditRequest request) {
        // TODO
        return null;
    }

    /**
     * Deletes the trip of the given id. Should only delete the trip
     * if it is owned by the user making the request.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable("id") String id) {
        // TODO
        return null;
    }

}
