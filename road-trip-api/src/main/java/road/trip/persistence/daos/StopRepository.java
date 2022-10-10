package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Stop;


import java.util.List;

@Repository
public interface StopRepository  extends JpaRepository<Stop, Long> {
    List<Stop> findByTripId(Long tripId);
}
