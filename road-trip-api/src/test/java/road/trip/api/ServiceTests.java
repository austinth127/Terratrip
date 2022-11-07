package road.trip.api;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import road.trip.api.location.LocationService;
import road.trip.api.trip.TripService;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.TripRepository;

import static org.mockito.Mockito.lenient;

public class ServiceTests {


    @Autowired
    ServiceTests(LocationService locationService, TripService tripService, UserService userService, TripRepository tripRepository) {
        this.locationService = locationService;
        this.tripService = tripService;
        this.userService = userService;
        this.tripRepository = tripRepository;
    }

    private LocationService locationService;
    private TripService tripService;
    private UserService userService;
    private TripRepository tripRepository;

    @Nested
    @DisplayName("Location Service Tests")
    class LocationServiceTests {
        @Test
        @DisplayName("Location Service: Create Location")
        @Ignore
        void createStop(){ //TODO: Call createLocation
            /**
            LocationRequest request = LocationRequest.builder()
                .name("Test Location").description("Test Location Description").rating(5)
                .coordX(100.1).coordY(100.2).geoType("Land").build();
            Location testLocation = locationService.createLocation(request);
            assertAll(
                ()->assertEquals(testLocation.getName(),"Test Location"),
                ()->assertEquals(testLocation.getDescription(),"Test Location Description"),
                ()->assertEquals(testLocation.getCoordX(),100.1),
                ()->assertEquals(testLocation.getCoordY(),100.2),
                ()->assertEquals(testLocation.getRating(),5),
                ()->assertEquals(testLocation.getGeoType(),"Land")
            );
             */
        }
    }
    @ExtendWith(MockitoExtension.class)
    @Nested
    @DisplayName("Trip Service Tests")
    class TripServiceTest{

        @Test
        @DisplayName("Trip Service: Create Trip")
        @Ignore
        void createTrip(){
            /**
            TripCreateRequest tripRequest = TripCreateRequest.builder()
                .name("Test Trip")
                .adventureLevel(4)
                .duration(100)
                .distance(101)
                .startDate(LocalDate.of(2022, Month.OCTOBER,9))
                .build();
            Long id = tripService.createTrip(tripRequest);
            Optional<Trip> optTrip = tripRepository.findById(id);
            assertAll(
                ()->assertNotNull(optTrip.orElse(null)),
                ()->assertEquals(optTrip.get().getName(),"Test Trip"),
                ()->assertEquals(optTrip.get().getAdventureLevel(),4),
                ()->assertEquals(optTrip.get().getDuration(),100),
                ()->assertEquals(optTrip.get().getDistance(),101),
                ()->assertEquals(optTrip.get().getStartDate(),LocalDate.of(2022, Month.OCTOBER,9))
            );

             */
        }
        @Mock
        TripService tripService;
        @Before
        void init(){

        }
        @Test
        @DisplayName("Edit Trip Request")
        void editTrip(){


        }
    }
    @Nested
    @DisplayName("User Service Tests")
    class UserServiceTest{

    }

}
