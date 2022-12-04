package road.trip.api.trip;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import road.trip.api.trip.response.TripResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.daos.UserRepository;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Trip;
import road.trip.persistence.models.User;
import road.trip.util.exceptions.ForbiddenException;
import road.trip.util.exceptions.NotFoundException;

import java.util.ArrayList;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Log4j2
@ActiveProfiles("test")
public class TripServiceTests {
    @Autowired TripRepository tripRepository;
    @Autowired UserRepository userRepository;
    @Autowired TripService tripService;

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
        tripRepository.deleteAll();
    }

    @Test
    void testGetTrip_200() {
        // Create a trip
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());

        // Get the trip
        TripResponse response = tripService.getTrip(trip.getId());

        // Verify response
        assertEquals(trip.getName(), response.getName());
        assertEquals(trip.getAdventureLevel().toString(), response.getAdvLevel());
    }

    @Test
    void testGetTrip_403() {
        // Create another user
        User notMe = userRepository.save(User.builder()
            .name("Not Me")
            .build());

        // Create a trip with that other user as its owner
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(notMe)
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());

        // Verify response
        assertThrows(ForbiddenException.class, () -> tripService.getTrip(trip.getId()));
    }

    @Test
    void testGetTrip_404() {
        assertThrows(NotFoundException.class, () -> tripService.getTrip(1L));
    }

}
