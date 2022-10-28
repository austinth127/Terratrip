package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Notification;
import road.trip.persistence.models.NotificationType;
import road.trip.persistence.models.Trip;
import road.trip.persistence.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> getNotificationsByUserAndSendAtBefore(User user, LocalDateTime cutoff);
    void deleteAllByTrip(Trip trip);

    Optional<Notification> findByTripAndType(Trip trip, NotificationType type);
}
