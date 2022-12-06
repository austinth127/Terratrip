package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Location;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByNameAndCoordXAndCoordY(String name, Double coordX, Double coordY);
    Optional<Location> findFirstByOtmIdOrOsmIdOrWikidataIdOrGeoapifyId(String otmId, Long osmId, String wikidataId, String geoapifyId);
}
