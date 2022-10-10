package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.requests.LocationRequest;
import road.trip.api.requests.StopRequest;
import road.trip.api.requests.TripCreateRequest;
import road.trip.api.requests.TripEditRequest;
import road.trip.api.responses.LocationResponse;
import road.trip.api.responses.StopResponse;
import road.trip.api.responses.TripResponse;
import road.trip.api.responses.ReducedTripResponse;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TripService {

    private final TripRepository tripRepository;
    private final LocationService locationService;
    private final UserService userService;

    /**
     * Gets a trip by id. Should only return the trip if the current user
     * owns that trip.
     */
    public TripResponse getTrip(Long id) {
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        TripResponse tr = null;

        if (optionalTrip.isEmpty()) {
            log.error("No trip found.");
        } else if (userService.user() == optionalTrip.get().getCreator()) {
            Trip t = optionalTrip.get();
            tr = new TripResponse(t);

        } else {
            log.error("Trip not owned by user.");
        }
        return tr;
    }

    public StopResponse addStop(StopRequest request) {
        return null; // TODO:
    }

    /**
     * Creates a trip and returns the id of the newly created trip
     */
    public Long createTrip(TripCreateRequest request) {
        Location start = locationService.createLocation(request.getStart());
        Location end = locationService.createLocation(request.getEnd());

        Trip trip = Trip.builder()
            .name(request.getName())
            .adventureLevel(AdventureLevel.valueOf(request.getAdventureLevel().toUpperCase()))
            .driveDuration(request.getDriveDuration())
            .distance(request.getDistance())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .creator(userService.user())
            .build();

        trip = tripRepository.save(trip);

        locationService.createStop(trip, start, 0);
        locationService.createStop(trip, end, 1);

        return trip.getId();
    }

    /**
     * Modifies the trip of the given id. Should only edit the trip
     * if it is owned by the currently authenticated user.
     * <p>
     * Use this method to add/delete locations as well.
     */
    public Long editTrip(long id, TripEditRequest request) {
        Optional<Trip> optTrip = tripRepository.findById(id);

        if (optTrip.isEmpty()) {
            log.error("No trip found.");
        } else if (userService.user() == optTrip.get().getCreator()) {
            Trip t = optTrip.get();
            if (request.getName() != null) {
                t.setName(request.getName());
            }
            if (request.getAdventureLevel() != null) {
                t.setAdventureLevel(request.getAdventureLevel());
            }
            if (request.getDriveDuration() != null) {
                t.setDriveDuration(request.getDriveDuration());
            }
            if (request.getDistance() != null) {
                t.setDistance(request.getDistance());
            }
            if (request.getStartDate() != null) {
                t.setStartDate(request.getStartDate());
            }
            if (request.getEndDate() != null) {
                t.setEndDate(request.getEndDate());
            }

            List<LocationRequest> stops = request.getStops();

            for (int i = 0; i < stops.size(); i++) {
//                if(stops.get(i).getId() == null){
                // create new Location and new Stop
                Location l = locationService.createLocation(stops.get(i));
                locationService.createStop(t, l, i);
//                }
//                else {
//                    // Update Stop to have correct Order
//                    locationService.updateOrder(t.get().getId(), stops.get(i).getId(), );
//                    if(l.isEmpty()) {
//                        log.error("LocationId not found.");
//                    }
//                    else {
//
//                    }
            }

            tripRepository.save(t);
            return t.getId();
        } else {
            log.error("Trip not owned by user.");
        }

        return null;
    }

    public void rateTrip(long id, double rating) {
        Optional<Trip> t = tripRepository.findById(id);
        if (t.isEmpty()) {
            log.error("Bro this is not your trip");
        } else {
            Trip trip = t.get();
            trip.setRating(rating);
            tripRepository.save(trip);
        }
    }

    /**
     * Deletes the trip of the given id. Should only delete the trip
     * if it is owned by the user making the request.
     */
    public void deleteTrip(Long id) {
        Optional<Trip> t = tripRepository.findById(id);
        if (t.isEmpty()) {
            log.error("No trip found.");
        } else if (userService.user() == t.get().getCreator()) {
            tripRepository.deleteById(id);
        } else {
            log.error("Trip not owned by user.");
        }
    }

    /**
     * Gets all the trips created by the user making the request
     */
    public List<ReducedTripResponse> getTrips() {
        List<Trip> trips = tripRepository.findByCreator_Id(userService.getId());
        return trips.stream().map(ReducedTripResponse::new).collect(Collectors.toList());

    }

}
