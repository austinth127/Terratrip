package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
