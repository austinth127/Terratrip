package road.trip.api.clients;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import road.trip.api.requests.LocationRequest;
import road.trip.api.requests.TripCreateRequest;
import road.trip.api.services.LocationService;
import road.trip.api.services.TripService;
import road.trip.api.services.UserService;
import road.trip.persistence.models.*;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
public class ServiceTests {

    @Autowired
    ServiceTests(LocationService locationService, TripService tripService, UserService userService) {
        this.locationService = locationService;
        this.tripService = tripService;
        this.userService = userService;
    }
    private final LocationService locationService;
    private final TripService tripService;
    private final UserService userService;
    @Nested
    @DisplayName("Location Service Tests")
    class LocationServiceTests {
        @Test
        @DisplayName("Location Service: Create Location")
        void createStop(){
            LocationRequest request = LocationRequest.builder()
                .name("Test Location").description("Test Location Description").rating(5)
                .coordX(100.1).coordY(100.2).geoType("Land").build();
            Location testLocation = locationService.createStop(request);
            assertAll(
                ()->assertEquals(testLocation.getName(),"Test Location"),
                ()->assertEquals(testLocation.getDescription(),"Test Location Description"),
                ()->assertEquals(testLocation.getCoordX(),100.1),
                ()->assertEquals(testLocation.getCoordY(),100.2),
                ()->assertEquals(testLocation.getRating(),5),
                ()->assertEquals(testLocation.getGeoType(),"Land")
            );
        }
    }
    @Nested
    @DisplayName("Trip Service Tests")
    class TripServiceTest{
        @Test
        @DisplayName("Trip Service: Create Trip")
        void createTrip(){
            TripCreateRequest tripRequest = TripCreateRequest.builder()
                .name("Test Trip")
                .adventureLevel(4)
                .duration(100)
                .distance(101)
                .startDate(LocalDateTime.of(2022, Month.OCTOBER,9,5,45))
                .build();
            Trip testTrip = null;
            assertAll(
                ()->assertEquals(testTrip.getName(), "Test Trip"),
                ()->assertEquals(testTrip.getAdventureLevel(),4),
                ()->assertEquals(testTrip.getDuration(),100),
                ()->assertEquals(testTrip.getDuration(),101),
                ()->assertEquals(testTrip.getStartDate(),LocalDateTime.of(2022, Month.OCTOBER,9,5,45))
            );

        }
    }
    @Nested
    @DisplayName("User Service Tests")
    class UserServiceTest{

    }

}
