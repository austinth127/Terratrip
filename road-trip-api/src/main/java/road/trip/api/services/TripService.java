package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import road.trip.api.requests.TripCreateRequest;
import road.trip.api.requests.TripEditRequest;
import road.trip.api.responses.TripResponse;
import road.trip.api.responses.TripsResponse;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Trip;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TripService {

    final TripRepository tripRepository;
    final LocationService locationService;

    @Deprecated
    public Trip createTripDeprecated(TripCreateRequest tripCreateRequest){
        System.out.println("here");
        Trip trip = Trip.builder()
            .name(tripCreateRequest.getName())
            .adventureLevel(tripCreateRequest.getAdventureLevel())
            .duration(tripCreateRequest.getDuration())
            .distance(tripCreateRequest.getDistance())
            .startDate(tripCreateRequest.getStartDate())
            .build();
        System.out.println("there");
        return tripRepository.save(trip);
    }

    /**
     * Gets a trip by id. Should only return the trip if the current user
     * owns that trip.
     */
    public ResponseEntity<TripResponse> getTrip(Long id){
        // TODO
        return null;
    }

    /**
     * Creates a trip and returns the id of the newly created trip
     */
    public ResponseEntity<Long> createTrip(TripCreateRequest request){
        // TODO
        return null;
    }

    /**
     * Modifies the trip of the given id. Should only edit the trip
     * if it is owned by the currently authenticated user.
     *
     * Use this method to add/delete locations as well.
     */
    public ResponseEntity<?> editTrip(String id, TripEditRequest request){
        // TODO
        return null;
    }

    /**
     * Deletes the trip of the given id. Should only delete the trip
     * if it is owned by the user making the request.
     */
    public ResponseEntity<?> deleteTrip(String id){
        // TODO
        return null;
    }

    /**
     * Gets all the trips created by the user making the request
     */
    public ResponseEntity<TripsResponse> getTrips(){
        // TODO
        return null;
    }

}
