package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.LocationRating;
import road.trip.persistence.models.User;

import java.util.List;
import java.util.Optional;

public interface LocationRatingRepository extends JpaRepository<LocationRating, Long>{
    LocationRating  findAllByRatingUserAndRatedLocation(User user, Optional<Location> location);
    Integer countAllByRatedLocation(Location location);
    List<LocationRating> findAllByRatedLocation(Location location);
    Boolean findByRatedLocation(Location e);

}
