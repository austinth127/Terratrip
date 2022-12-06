package road.trip.api.trip;

import lombok.extern.log4j.Log4j2;
import net.bytebuddy.asm.Advice;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import road.trip.api.notification.NotificationService;
import road.trip.api.notification.response.NotificationResponse;
import road.trip.api.trip.request.TripEditRequest;
import road.trip.api.trip.response.TripResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.NotificationRepository;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.daos.UserRepository;
import road.trip.persistence.models.*;
import road.trip.util.exceptions.ForbiddenException;
import road.trip.util.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Log4j2
@ActiveProfiles("test")
@DisplayName("Notification Service Tests")
public class NotificationServiceTests {
    @Autowired TripRepository tripRepository;
    @Autowired UserRepository userRepository;
    @Autowired TripService tripService;
    @Autowired NotificationService notificationService;
    @Autowired NotificationRepository notificationRepository;
    @MockBean UserService userService;

    @BeforeEach
    void init() {
        User user = userRepository.save(User.builder()
            .name("Test User")
            .emailAddress("test@test.com")
            .username("tester")
            .build());
        when(userService.user()).thenReturn(user);
    }

    @AfterEach
    void cleanUp() {
        notificationRepository.deleteAll();
        tripRepository.deleteAll();
    }

    @Test
    @DisplayName("get notifications test")
    void getNotificationsTest(){
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .startDate(LocalDate.of(2022,1,1))
            .endDate(LocalDate.of(2023,1,10))
            .build());
        List<Notification> enqueuedNotifications = notificationService.enqueueNotifications(trip);
        assertEquals(3,enqueuedNotifications.size());
    }
    @Test
    @DisplayName("delete notifications test 200")
    void deleteNotificationTest_200(){
        Notification test = notificationRepository.save(Notification.builder()
            .trip(null)
            .type(NotificationType.NEW_ACCOUNT)
            .user(userService.user())
            .build());
        notificationService.deleteNotification(test.getId());
        assertEquals(0,notificationService.getNotifications().size());


    }
    @Test
    @DisplayName("delete notifications test 400")
    void deleteNotificationTest_400(){
        assertThrows(NotFoundException.class,()->notificationService.deleteNotification(1L));

    }
    @Test
    @DisplayName("delete notifications test 403")
    void deleteNotificationTest_403(){
        User notMe = userRepository.save(User.builder()
            .name("Not Me")
            .build());
        Notification test = notificationRepository.save(Notification.builder()
            .trip(null)
            .type(NotificationType.NEW_ACCOUNT)
            .user(notMe)
            .build());
        assertThrows(ForbiddenException.class,()->notificationService.deleteNotification(test.getId()));

    }
    @Test
    @DisplayName("update notifications test 200")
    void updateNotificationTest(){
        //Create Trip where the Upcoming Trip Day Notification is meant to be sent.
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .startDate(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth()))
            .endDate(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth()))
            .build());
        List<Notification> enqueuedNotifications = notificationService.enqueueNotifications(trip);
        //Change the trip date so the Upcoming Week Notification should be sent.
        trip.setStartDate(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth()+7));
        trip.setEndDate(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth()+7));
        //Call Update
        notificationService.updateNotifications(trip);
        List<NotificationResponse> a = notificationService.getNotifications();
        //Confirm that the correct notification is sent due to the changes to trip date and update.
        assertEquals(NotificationType.UPCOMING_TRIP_WEEK,a.get(0).getType());
    }
    @Test
    @DisplayName("enqueue notifications test 200")
    void enqueueNotificationsTest(){
        //Enqueue Notifications that should be sent
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .startDate(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth()))
            .endDate(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),LocalDate.now().getDayOfMonth()))
            .build());
        //3 Notifications should be queued.
        List<Notification> enqueuedNotifications = notificationService.enqueueNotifications(trip);
        assertEquals(3,enqueuedNotifications.size());

    }
    @Test
    @DisplayName("enqueue notifications test new user 200")
    void enqueueNotificationNewUserTest(){
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .startDate(LocalDate.of(2022,1,1))
            .endDate(LocalDate.of(2023,1,10))
            .build());
        //Enqueue only the new user notification.
        List<Notification> enqueuedNotifications = notificationService.enqueueNotifications(userService.user());
        assertAll(
            ()->assertEquals(1,enqueuedNotifications.size()),
            ()->assertEquals(NotificationType.NEW_ACCOUNT,enqueuedNotifications.get(0).getType())
        );
    }



}
