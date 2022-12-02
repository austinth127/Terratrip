package road.trip.api.trip;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.location.LocationService;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.notification.NotificationService;
import road.trip.api.trip.request.TripCreateRequest;
import road.trip.api.trip.request.TripEditRequest;
import road.trip.api.trip.response.TripResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.StopRepository;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;
import road.trip.util.exceptions.ForbiddenException;
import road.trip.util.exceptions.NotFoundException;

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
    private final NotificationService notificationService;
    private final StopRepository stopRepository;

    /**
     * Gets a trip by id. Should only return the trip if the current user
     * owns that trip.
     */
    public TripResponse getTrip(Long id) {
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        TripResponse tr = null;

        if (optionalTrip.isEmpty()) {
            throw new NotFoundException("Trip not found.");
        } else if (userService.user().equals(optionalTrip.get().getCreator())) {
            Trip t = optionalTrip.get();
            tr = new TripResponse(t, optionalTrip.get().getCreator(), locationService);
        } else {
            throw new ForbiddenException("Trip not owned by user.");
        }
        return tr;
    }

    /**
     * Creates a trip and returns the id of the newly created trip
     */
    public Long createTrip(TripCreateRequest request) {
        System.out.println(request.toString());
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
            .start(start)
            .end(end)
            .build();

        trip = tripRepository.save(trip);

        notificationService.enqueueNotifications(trip);

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
            throw new NotFoundException("Trip not found.");
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
            if (request.getPlaylistId() != null) {
                t.setPlaylistId(request.getPlaylistId());
            }
            if (request.getStart() != null) {
                Location l = locationService.createLocation(request.getStart());
                t.setStart(l);
            }
            if (request.getEnd() != null) {
                Location l = locationService.createLocation(request.getEnd());
                t.setEnd(l);
            }
            if(request.getStops() != null){
                List<LocationRequest> stops = request.getStops();

//                 Remove Stops in trip
                List<Stop> oldStops = stopRepository.findByTrip_Id(id);
                for (int i = 0; i < oldStops.size(); i++) {
                    stopRepository.deleteById(oldStops.get(i).getStopId());
                }


                oldStops = stopRepository.findByTrip_Id(id);

                if (oldStops.size() == 0) {
                    // Re-add stops
                    for (int i = 0; i < stops.size(); i++) {
                        Location l = locationService.createLocation(stops.get(i));
                        locationService.createStop(t, l, i);
                    }
                }
            }

            tripRepository.save(t);

            notificationService.updateNotifications(t);

            return t.getId();
        } else {
            throw new ForbiddenException("Trip not owned by user.");
        }

    }

    public void rateTrip(long id, double rating) {
        Optional<Trip> t = tripRepository.findById(id);
        if (t.isEmpty()) {
            throw new NotFoundException("Trip not found.");
        } else if (userService.user() == t.get().getCreator()) {
            Trip trip = t.get();
            trip.setRating(rating);
            tripRepository.save(trip);
        } else {
            throw new ForbiddenException("Trip not owned by user.");
        }
    }

    /**
     * Deletes the trip of the given id. Should only delete the trip
     * if it is owned by the user making the request.
     */
    public void deleteTrip(Long id) {
        Optional<Trip> t = tripRepository.findById(id);
        if (t.isEmpty()) {
            throw new NotFoundException("Trip not found.");
        } else if (userService.user() == t.get().getCreator()) {
            List<Stop> s = stopRepository.findByTrip_Id(id);

            for (int i = 0; i < s.size(); i++) {
                stopRepository.deleteById(s.get(i).getStopId());
            }

            notificationService.deleteNotifications(t.get());

            tripRepository.deleteById(id);
        } else {
            throw new ForbiddenException("Trip not owned by user.");
        }
    }

    /**
     * Gets all the trips created by the user making the request
     */
    public List<TripResponse> getTrips() {
        List<Trip> trips = tripRepository.findByCreator_Id(userService.getId());
        return trips.stream().map(t -> new TripResponse(t, t.getCreator(), locationService)).collect(Collectors.toList());
    }

}
