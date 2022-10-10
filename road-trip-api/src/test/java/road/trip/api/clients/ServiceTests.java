package road.trip.api.clients;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import road.trip.api.requests.StopRequest;
import road.trip.api.requests.TripCreateRequest;
import road.trip.api.services.StopService;
import road.trip.api.services.TripService;
import road.trip.api.services.UserService;
import road.trip.persistence.daos.StopRepository;
import road.trip.persistence.models.*;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ServiceTests {
    private final StopService stopService;
    private final TripService tripService;
    private final UserService userService;
    @Nested
    @DisplayName("Stop Service Tests")
    class StopServiceTests{
        @Test
        @DisplayName("Stop Service: Create Stop")
        void createStop(){
            StopRequest request = StopRequest.builder()
                .name("Test Stop").description("Test Stop Description").rating(5)
                .coordX(100.1).coordY(100.2).geoType("Land").build();
            Stop testStop = stopService.createStop(request);
            assertAll(
                ()->assertEquals(testStop.getName(),"Test Stop"),
                ()->assertEquals(testStop.getDescription(),"Test Stop Description"),
                ()->assertEquals(testStop.getCoordX(),100.1),
                ()->assertEquals(testStop.getCoordY(),100.2),
                ()->assertEquals(testStop.getRating(),5),
                ()->assertEquals(testStop.getGeoType(),"Land")
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
            Trip testTrip = tripService.createTrip(tripRequest);
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
