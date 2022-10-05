package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Trip;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TripService {

    final TripRepository tripRepository;

    public void createTrip(){}

    //TODO: IDK if getReference is right
    public Trip getTrip(Long tripId){
        return tripRepository.getReferenceById(tripId);
    }

    public void addStop(){}


}
