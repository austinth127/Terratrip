package road.trip.api.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.util.exceptions.NotFoundException;
import road.trip.api.notification.response.NotificationResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.NotificationRepository;
import road.trip.persistence.models.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
     * Gets the list of notifications that should be sent to the authenticated user
     *
     * @return the list of notifications
     */
    public List<NotificationResponse> getNotifications() {
        // Get the list of notifications that should be sent to the authenticated user
        List<Notification> notifications = notificationRepository.getNotificationsByUserAndSendAtBeforeAndExpireAtAfter(userService.user(), LocalDateTime.now(), LocalDateTime.now());

        // Convert the notification into a format that the frontend wants
        return notifications.stream()
            .map(NotificationResponse::new)
            .collect(Collectors.toList());
    }

    public List<Notification> updateNotifications(Trip trip) {
        deleteNotifications(trip);
        return enqueueNotifications(trip);
    }

    public void deleteNotification(Long id) throws NotFoundException {
        if (!notificationRepository.existsById(id)) {
            throw new NotFoundException();
        }
        notificationRepository.deleteById(id);
    }

    public void deleteNotifications(Trip trip) {
        List<Notification> n = notificationRepository.findByTrip_Id(trip.getId());

        for (int i = 0; i < n.size(); i++) {
            notificationRepository.deleteById(n.get(i).getId());
        }
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
        Notification upcomingWeekNotification = createUpcomingTripWeekNotification(trip);
        Notification upcomingDayNotification = createUpcomingTripDayNotification(trip);
        Notification completedNotification = createCompletedNotification(trip);

        List<Notification> nList = new ArrayList<>();

        nList.add(notificationRepository.save(upcomingWeekNotification));
        nList.add(notificationRepository.save(upcomingDayNotification));
        nList.add(notificationRepository.save(completedNotification));

        return nList;
    }

    public List<Notification> enqueueNotifications(User user) {
        Notification newAccountNotification = createNewAccountNotification(user);

        List<Notification> nList = new ArrayList<>();

        nList.add(notificationRepository.save(newAccountNotification));

        return nList;
    }



    private Notification createNewAccountNotification(User user) {
        return Notification.builder()
            .user(user)
            .sendAt(LocalDateTime.now())
            .expireAt(LocalDateTime.now().plusYears(1))
            .type(NotificationType.NEW_ACCOUNT)
            .build();
    }

    /**
     * Generates upcoming trip notification 5 days before the start date
     */
    private Notification createUpcomingTripWeekNotification(Trip trip) {
        return Notification.builder()
            .trip(trip)
            .user(trip.getCreator())
            .sendAt(trip.getStartDate().minusDays(7).atStartOfDay())
            .expireAt(trip.getStartDate().minusDays(1).atStartOfDay())
            .type(NotificationType.UPCOMING_TRIP_WEEK)
            .build();
    }

    private Notification createUpcomingTripDayNotification(Trip trip) {
        return Notification.builder()
            .trip(trip)
            .user(trip.getCreator())
            .sendAt(trip.getStartDate().minusDays(1).atStartOfDay())
            .expireAt(trip.getStartDate().atStartOfDay())
            .type(NotificationType.UPCOMING_TRIP_DAY)
            .build();
    }


    /**
     * Generates trip completion notification at 8pm on the end date
     */
    private Notification createCompletedNotification(Trip trip) {
         return Notification.builder()
            .trip(trip)
            .user(trip.getCreator())
            .sendAt(trip.getEndDate().plusDays(1).atStartOfDay())
            .expireAt(LocalDateTime.now().plusYears(1))
            .type(NotificationType.COMPLETED_TRIP)
            .build();
    }
}
