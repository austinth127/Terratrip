package road.trip.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.requests.StopRequest;
import road.trip.persistence.daos.StopRepository;
import road.trip.persistence.models.Stop;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StopService {

    private final StopRepository stopRepository;

    public Stop createStop(StopRequest stopRequest) {

        Stop stop = Stop.builder()
            .name(stopRequest.getName())
            .description(stopRequest.getDescription())
            .rating(stopRequest.getRating())
            .build();

        return stopRepository.save(stop);
    }
}
