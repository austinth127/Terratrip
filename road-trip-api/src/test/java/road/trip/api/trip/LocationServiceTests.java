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
import road.trip.api.location.LocationService;
import road.trip.api.location.request.LocationRequest;
import road.trip.api.location.response.LocationResponse;
import road.trip.api.trip.request.TripEditRequest;
import road.trip.api.trip.response.TripResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.*;
import road.trip.persistence.models.*;
import road.trip.util.exceptions.ForbiddenException;
import road.trip.util.exceptions.NotFoundException;

import java.util.*;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Log4j2
@ActiveProfiles("Location Service Tests")
public class LocationServiceTests {
    @Autowired TripRepository tripRepository;
    @Autowired LocationRepository locationRepository;
    @Autowired UserRepository userRepository;
    @Autowired LocationService locationService;
    @Autowired StopRepository stopRepository;
    @Autowired LocationRatingRepository locationRatingRepository;
    @Autowired CategoryRepository categoryRepository;
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
        categoryRepository.deleteAll();
        stopRepository.deleteAll();
        locationRatingRepository.deleteAll();
        locationRepository.deleteAll();
        tripRepository.deleteAll();
    }
    @Test
    void createLocationTest_200(){
        LocationRequest request = LocationRequest.builder()
            .categories(null)
            .coordX(100.0)
            .coordY(100.0)
            .description("test location")
            .name("test location name")
            .build();
        Location test = locationService.createLocation(request);
        assertAll(
            ()->assertEquals(request.getCoordY(),test.getCoordY()),
            ()->assertEquals(request.getCoordX(),test.getCoordX()),
            ()->assertEquals(request.getName(),test.getName()),
            ()->assertEquals(request.getDescription(),test.getDescription())
        );


    }
    @Test
    void findLocationTest_200(){
        LocationRequest request = LocationRequest.builder()
            .categories(null)
            .coordX(100.0)
            .coordY(100.0)
            .description("test location")
            .name("test location name")
            .build();
        Location test = locationService.createLocation(request);
        Location found = locationService.findLocation(request);
        assertAll(
            ()->assertEquals(test.getName(),found.getName()),
            ()->assertEquals(test.getCoordY(),found.getCoordY()),
            ()->assertEquals(test.getCoordX(),found.getCoordX()),
            ()->assertEquals(test.getDescription(),found.getDescription())
        );
    }

    @Test
    void addLocationRatingTest_200(){
        LocationRequest request = LocationRequest.builder()
            .categories(null)
            .coordX(100.0)
            .coordY(100.0)
            .description("test location")
            .name("test location name")
            .build();
        Location test = locationService.createLocation(request);
        locationService.addLocationRating(test.getId(),7.7);
        assertEquals(7.7,locationRatingRepository.findByUserAndLocation(userService.user(), test).get().getRating());
    }
    // Don't need to test this because any location can be rated by any user.
    //    @Test
    //    void addLocationRatingTest_403(){
    //        User notMe = userRepository.save(User.builder()
    //            .name("Not Me")
    //            .build());
    //    }
    //Location to be rated is not found.
    @Test
    void addLocationRatingTest_400(){
        assertThrows(NoSuchElementException.class,()->locationService.addLocationRating(1L,0.0));

    }
    @Test
    void getRatingByLocationAndUserTest_200(){
        LocationRequest request = LocationRequest.builder()
            .categories(null)
            .coordX(100.0)
            .coordY(100.0)
            .description("test location")
            .name("test location name")
            .build();
        Location test = locationService.createLocation(request);
        locationService.addLocationRating(test.getId(),7.7);
        assertEquals(7.7,locationService.getRatingByLocationAndUser(test,userService.user()));
    }
    @Test
    void getAverageRatingTest_200(){
        LocationRequest request = LocationRequest.builder()
            .categories(null)
            .coordX(100.0)
            .coordY(100.0)
            .description("test location")
            .name("test location name")
            .build();
        Location test = locationService.createLocation(request);
        locationService.addLocationRating(test.getId(),7.7);
        assertEquals(7.7,locationService.getAverageRating(test));

    }
    @Test
    void getAdventureLevelTest_200(){
        Category c = categoryRepository.save(Category.builder()
            .adventureLevel(AdventureLevel.EXTREME)
            .uiName("test").build());
        LocationRequest request = LocationRequest.builder()
            .categories(Arrays.asList("test"))
            .coordX(100.0)
            .coordY(100.0)
            .description("test location")
            .name("test location name")
            .build();
        Location test = locationService.createLocation(request);
        assertEquals(AdventureLevel.EXTREME,locationService.getAdventureLevel(test));
    }
    @Test
    void createStopTest_200(){
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .build());
        LocationRequest request = LocationRequest.builder()
            .categories(null)
            .coordX(100.0)
            .coordY(100.0)
            .description("test location")
            .name("test location name")
            .build();
        Location test = locationService.createLocation(request);
        Stop testStop = locationService.createStop(trip,test,1);
        assertEquals(testStop,stopRepository.findById(testStop.getStopId()).get());
    }
//    @Test
//    void createStopTest_400(){
//        Trip trip = tripRepository.save(Trip.builder()
//            .name("Test Trip")
//            .creator(userService.user())
//            .stops(new ArrayList<>())
//            .adventureLevel(AdventureLevel.HIGH)
//            .build());
//        Location test = Location.builder()
//            .description("test location not saved")
//            .categories(null)
//            .coordX(100.0)
//            .coordY(100.0)
//            .name("test location name")
//            .build();
//        assertThrows(NoSuchElementException.class, ()->locationService.createStop(trip,test,1));
//
//    }


}