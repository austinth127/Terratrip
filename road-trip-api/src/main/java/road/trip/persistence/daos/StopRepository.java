package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Stop;


import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface StopRepository  extends JpaRepository<Stop, Long> {
    List<Stop> findByTrip_Id(Long tripId);
    List<Stop> findByTrip_IdAndLocation_IdAndOrder(Long tripId, Long locId, int order);
}
