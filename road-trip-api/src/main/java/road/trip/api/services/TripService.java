package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.requests.StopRequest;
import road.trip.api.requests.TripRequest;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TripService {

    final TripRepository tripRepository;
    final StopService stopService;

    public Trip createTrip(TripRequest tripRequest){
        System.out.println("here");
        Trip trip = Trip.builder()
            .name(tripRequest.getName())
            .adventureLevel(tripRequest.getAdventureLevel())
            .duration(tripRequest.getDuration())
            .distance(tripRequest.getDistance())
            .startDate(tripRequest.getStartDate())
            .build();
        System.out.println("there");
        return tripRepository.save(trip);
    }

    //TODO: IDK if getReference is right
    public Trip getTrip(Long tripId){
        return tripRepository.getReferenceById(tripId);
    }

}
