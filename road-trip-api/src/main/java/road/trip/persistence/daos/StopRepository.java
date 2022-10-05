package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
}
