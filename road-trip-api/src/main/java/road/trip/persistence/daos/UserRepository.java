package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}