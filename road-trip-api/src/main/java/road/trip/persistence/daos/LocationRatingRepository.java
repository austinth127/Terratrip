package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.LocationRating;
import road.trip.persistence.models.User;

import java.util.Optional;

public interface LocationRatingRepository extends JpaRepository<LocationRating, Long>{
    Integer countAllByRatingUser(User user);
    LocationRating  findByUserAndLocation(User user, Optional<Location> location);
    Integer countAllByRatedLocation(Location location);
}
