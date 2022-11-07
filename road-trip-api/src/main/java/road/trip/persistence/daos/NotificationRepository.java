package road.trip.persistence.daos;

import org.aspectj.weaver.ast.Not;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationsByUserAndSendAtBeforeAndExpireAtAfter(User user, LocalDateTime cutoff, LocalDateTime cutoff2);
    void deleteAllByTrip(Trip trip);
    List<Notification> findByTrip_Id(Long trip_Id);


    Optional<Notification> findByTripAndType(Trip trip, NotificationType type);
}
