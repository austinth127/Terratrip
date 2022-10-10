package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.requests.TripCreateRequest;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Trip;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TripService {

    final TripRepository tripRepository;
    final StopService stopService;

    public Trip createTrip(TripCreateRequest tripCreateRequest){
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

    //TODO: IDK if getReference is right
    public Trip getTrip(Long tripId){
        return tripRepository.getReferenceById(tripId);
    }

}
