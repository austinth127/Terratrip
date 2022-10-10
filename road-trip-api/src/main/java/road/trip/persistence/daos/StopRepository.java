package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Stop;

@Repository
public interface StopRepository extends JpaRepository<Stop, Long> {
}
