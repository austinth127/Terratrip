package road.trip.api.trip;

import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import road.trip.api.trip.request.TripEditRequest;
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
public class TripServiceTests {
    @Autowired TripRepository tripRepository;
    @Autowired UserRepository userRepository;
    @Autowired TripService tripService;

    @MockBean
    UserService userService;

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
    @DisplayName("Test Get Trip 200")
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
    @DisplayName("Test Get Trip 403")
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
    @DisplayName("Test Get Trip 404")
    void testGetTrip_404() {
        assertThrows(NotFoundException.class, () -> tripService.getTrip(1L));
    }


    @Test
    @DisplayName("Test Rate Trip 200")
    void testRateTrip_200(){
        // Create a trip
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());
        tripService.rateTrip(trip.getId(),9.7);
        // Get the trip
        TripResponse response = tripService.getTrip(trip.getId());
        assertEquals(9.7,response.getRating());
    }
    @Test
    @DisplayName("Test Get Trip 403")
    void testRateTrip_403(){
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
        assertThrows(ForbiddenException.class, () -> tripService.rateTrip(trip.getId(),9.7));
    }
    @Test
    @DisplayName("Test Get Trip 404")
    void testRateTrip_404(){
        assertThrows(NotFoundException.class, () -> tripService.rateTrip(1L,9.7));
    }
    @Test
    @DisplayName("Test Create Trip 200")
    void createTripTest_200(){

    }
    @Test
    @DisplayName("Test Edit Trip 200")
    void editTripTest_200(){
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());
        TripEditRequest editTrip = TripEditRequest.builder()
            .name("Test Trip Edit Name")
            .adventureLevel(AdventureLevel.EXTREME)
            .build();
        tripService.editTrip(trip.getId(),editTrip);
        TripResponse response = tripService.getTrip(trip.getId());
        assertAll(
            ()->assertEquals("Test Trip Edit Name",response.getName()),
            ()->assertEquals(AdventureLevel.EXTREME.toString(),response.getAdvLevel())
        );
    }
    @Test
    @DisplayName("Test Get Trip 403")
    void editTripTest_403(){
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
        TripEditRequest editTrip = TripEditRequest.builder()
            .name("Test Trip Edit Name")
            .adventureLevel(AdventureLevel.EXTREME)
            .build();

        // Verify response
        assertThrows(ForbiddenException.class, () -> tripService.editTrip(trip.getId(),editTrip));
    }
    @Test
    @DisplayName("Test Edit Trip 404")
    void editTripTest_404(){
        TripEditRequest editTrip = TripEditRequest.builder()
            .name("Test Trip Edit Name")
            .adventureLevel(AdventureLevel.EXTREME)
            .build();
        assertThrows(NotFoundException.class, () -> tripService.editTrip(1L,editTrip));
    }
    @Test
    @DisplayName("Delete Trip 200")
    void deleteTripTest_200(){
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());
        tripService.deleteTrip(trip.getId());
        assertThrows(NotFoundException.class,()->tripService.getTrip(trip.getId()));
    }
    //trip not owned by current user.
    @Test
    @DisplayName("Delete Trip 403")
    void deleteTripTest_403(){
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
        assertThrows(ForbiddenException.class, () -> tripService.deleteTrip(trip.getId()));

    }
    //trip not found.
    @Test
    @DisplayName("Delete Trip 404")

    void deleteTripTest_404(){
        assertThrows(NotFoundException.class, () -> tripService.deleteTrip(1L));
    }
    @Test
    @DisplayName("Get Trips 200")
    void getTripsTest(){
        List<Trip> testTrips = new ArrayList<>();
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());

        Trip trip1 = tripRepository.save(Trip.builder()
            .name("Test Trip1")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());
        Trip trip2 = tripRepository.save(Trip.builder()
            .name("Test Trip2")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());
        testTrips.add(trip);
        testTrips.add(trip1);
        testTrips.add(trip2);
        assertEquals(testTrips.size(),tripService.getTrips().size());
    }
}
