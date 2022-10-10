package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import road.trip.api.requests.StopRequest;
import road.trip.api.requests.TripCreateRequest;
import road.trip.api.requests.TripEditRequest;
import road.trip.api.responses.StopResponse;
import road.trip.api.responses.TripResponse;
import road.trip.api.responses.TripsResponse;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TripService {

    private final TripRepository tripRepository;
    private final LocationService locationService;

    /**
     * Gets a trip by id. Should only return the trip if the current user
     * owns that trip.
     */
    public TripResponse getTrip(Long id){
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        TripResponse tr = null;

        if (optionalTrip.isPresent()) {
            Trip t = optionalTrip.get();
            tr = new TripResponse(t.getId(), t.getName(), t.getStartDate(), t.getDuration(), t.getAdventureLevel());
            tr.setStops(locationService.getLocationsForTrip(t.getId()));
        }
        return tr;
    }

    public StopResponse addStop(StopRequest request) {
        return null; // TODO:
    }

    /**
     * Creates a trip and returns the id of the newly created trip
     */
    public Long createTrip(TripCreateRequest request){
        Trip trip = Trip.builder()
                .name(request.getName())
                .adventureLevel(request.getAdventureLevel())
                .duration(request.getDuration())
                .distance(request.getDistance())
                .startDate(request.getStartDate())
                .build();
        return tripRepository.save(trip).getId();
    }

    /**
     * Modifies the trip of the given id. Should only edit the trip
     * if it is owned by the currently authenticated user.
     *
     * Use this method to add/delete locations as well.
     */
    public Object editTrip(String id, TripEditRequest request){
        // TODO
        return null;
    }

    /**
     * Deletes the trip of the given id. Should only delete the trip
     * if it is owned by the user making the request.
     */
    public Object deleteTrip(String id){
        // TODO
        return null;
    }

    /**
     * Gets all the trips created by the user making the request
     */
    public List<TripsResponse> getTrips(){
        // TODO
        return null;
    }

}
