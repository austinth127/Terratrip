package road.trip.api.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.requests.TripCreateRequest;
import road.trip.api.requests.TripEditRequest;
import road.trip.api.requests.TripRateRequest;
import road.trip.api.responses.TripResponse;
import road.trip.api.responses.ReducedTripResponse;
import road.trip.api.services.TripService;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/trip")
public class TripController {

    final TripService tripService;

    /**
     * Gets all the trips created by the user making the request
     */
    @GetMapping
    public ResponseEntity<List<ReducedTripResponse>> getTrips() {
        return ResponseEntity.ok(tripService.getTrips());
    }

    /**
     * Gets a trip by id. Should only return the trip if the current user
     * owns that trip.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TripResponse> getTrip(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tripService.getTrip(id));
    }

    /**
     * Creates a trip and returns the id of the newly created trip
     */
    @PostMapping
    public ResponseEntity<Long> createTrip(@RequestBody TripCreateRequest request) {
        return ResponseEntity.ok(tripService.createTrip(request));
    }

    /**
     * Modifies the trip of the given id. Should only edit the trip
     * if it is owned by the user making the request.
     *
     * Use this request to add/delete locations as well.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> editTrip(@PathVariable("id") long id, @RequestBody TripEditRequest request) {
        tripService.editTrip(id, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/rate")
    public ResponseEntity<?> rateTrip(@PathVariable("id") long id, @RequestBody TripRateRequest request) {
        tripService.rateTrip(id, request.getRating());
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes the trip of the given id. Should only delete the trip
     * if it is owned by the user making the request.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrip(@PathVariable("id") long id) {
        tripService.deleteTrip(id);
        return ResponseEntity.ok().build();
    }

}
