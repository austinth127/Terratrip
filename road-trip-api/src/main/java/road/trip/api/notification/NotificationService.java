package road.trip.api.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.notification.response.NotificationResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.NotificationRepository;
import road.trip.persistence.models.Notification;
import road.trip.persistence.models.NotificationType;
import road.trip.persistence.models.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
     * and clears them from the database so that they only get sent to the user once
     *
     * @return the list of notifications
     */
    public List<NotificationResponse> getNotifications() {
        // Get the list of notifications that should be sent to the authenticated user
        List<Notification> notifications = notificationRepository.getNotificationsByUserAndSendAtBefore(userService.user(), LocalDateTime.now());

        // Convert the notification into a format that the frontend wants
        List<NotificationResponse> response = notifications.stream()
            .map(this::getResponse)
            .collect(Collectors.toList());

        // Remove the notifications that have been processed
        notifications.forEach(notificationRepository::delete);

        return response;
    }
}
