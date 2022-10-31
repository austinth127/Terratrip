package road.trip.api.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.util.exceptions.NotFoundException;
import road.trip.api.notification.response.NotificationResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.NotificationRepository;
import road.trip.persistence.models.Notification;
import road.trip.persistence.models.NotificationType;
import road.trip.persistence.models.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    /**
     * Creates a response object from the given Notification object
     *
     * @param notification the notification for which to generate a response
     * @return the response object
     */
    private NotificationResponse getResponse(Notification notification) {
        Trip trip = notification.getTrip();
        NotificationResponse response = NotificationResponse.builder()
            .id(notification.getId())
            .type(notification.getType())
            .notifiedAt(LocalDateTime.now())
            .build();

        switch (notification.getType()) {
            case UPCOMING_TRIP -> response.setText(String.format("Your \"%s\" trip is coming up in %d days", trip.getName(), DAYS.between(LocalDate.now(), trip.getStartDate())));
            case COMPLETED_TRIP -> response.setText(String.format("%s: Trip Complete!", trip.getName()));
            default -> throw new IllegalStateException("Oops, not implemented");
        }

        return response;
    }

    /**
     * Gets the list of notifications that should be sent to the authenticated user
     *
     * @return the list of notifications
     */
    public List<NotificationResponse> getNotifications() {
        // Get the list of notifications that should be sent to the authenticated user
        List<Notification> notifications = notificationRepository.getNotificationsByUserAndSendAtBefore(userService.user(), LocalDateTime.now());

        // Convert the notification into a format that the frontend wants
        return notifications.stream()
            .map(this::getResponse)
            .collect(Collectors.toList());
    }

    public List<Notification> editNotifications(Trip trip) {
        Optional<Notification> upcomingNotification = notificationRepository.findByTripAndType(trip, NotificationType.UPCOMING_TRIP);
        Optional<Notification> completedNotification = notificationRepository.findByTripAndType(trip, NotificationType.COMPLETED_TRIP);

        Notification un = createUpcomingTripNotification(trip);
        Notification cn = createCompletedNotification(trip);

        upcomingNotification.ifPresent((n) -> {
            notificationRepository.delete(n);
            notificationRepository.save(un);
        });
        completedNotification.ifPresent((n) -> {
            notificationRepository.delete(n);
            notificationRepository.save(cn);
        });

        return List.of(un, cn);
    }

    public void deleteNotification(Long id) throws NotFoundException {
        if (!notificationRepository.existsById(id)) {
            throw new NotFoundException();
        }
        notificationRepository.deleteById(id);
    }

    public void deleteNotifications(Trip trip) {
        notificationRepository.deleteAllByTrip(trip);
    }

    /**
     * Adds notifications relevant to the trip to the notification priority queue.
     * These notifications will get pulled from the queue when the current time is later than the
     * notification's "sendAt" time.
     *
     * @param trip the trip for which to generate notifications
     * @return the list of created notifications
     */
    public List<Notification> enqueueNotifications(Trip trip) {
        Notification upcomingNotification = createUpcomingTripNotification(trip);
        Notification completedNotification = createCompletedNotification(trip);

        notificationRepository.save(upcomingNotification);
        notificationRepository.save(completedNotification);

        return List.of(upcomingNotification, completedNotification);
    }


    /**
     * Generates upcoming trip notification 5 days before the start date
     */
    private Notification createUpcomingTripNotification(Trip trip) {
        return Notification.builder()
            .trip(trip)
            .user(trip.getCreator())
            .sendAt(trip.getStartDate().minusDays(5).atStartOfDay())
            .type(NotificationType.UPCOMING_TRIP)
            .build();
    }

    /**
     * Generates trip completion notification at 8pm on the end date
     */
    private Notification createCompletedNotification(Trip trip) {
         return Notification.builder()
            .trip(trip)
            .user(trip.getCreator())
            .sendAt(trip.getEndDate().atTime(20, 0))
            .type(NotificationType.COMPLETED_TRIP)
            .build();
    }
}
