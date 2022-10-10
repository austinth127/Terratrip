package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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


    public ResponseEntity<TripResponse> getTrip(@PathVariable("id") Long id){
        // TODO
        return null;
    }


    public ResponseEntity<Long> createTrip(@RequestBody TripCreateRequest request){
        // TODO
        return null;
    }

    public ResponseEntity<?> editTrip(@PathVariable("id") String id, @RequestBody TripEditRequest request){
        // TODO
        return null;
    }
    public ResponseEntity<?> deleteTrip(@PathVariable("id") String id){
        // TODO
        return null;
    }
    public ResponseEntity<TripsResponse> getTrips(){
        // TODO
        return null;
    }

}
