package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Trip;
import road.trip.persistence.models.User;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByCreator_Id(Long userID);

}
