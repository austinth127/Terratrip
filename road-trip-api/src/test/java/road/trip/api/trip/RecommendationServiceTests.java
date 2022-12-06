package road.trip.api.trip;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.jacquet80.minigeo.MapWindow;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import road.trip.api.location.RecommendationService;
import road.trip.api.location.response.RecommendationResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.CategoryRepository;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.daos.UserRepository;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Trip;
import road.trip.persistence.models.User;
import road.trip.util.exceptions.ForbiddenException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.lang.Thread.sleep;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Why doesn't the user own the trip.
 * Why is UserService a mockbean?
 * How do I test startRecommendationRequests?
 * Can you help me identify all the cases to test for?
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Log4j2
@ActiveProfiles("test")
public class RecommendationServiceTests {

    @Autowired
    RecommendationService recommendationService;
    @Autowired
    TripRepository tripRepository;
    @Autowired
    TripService tripService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @MockBean
    UserService userService;

    @BeforeEach
    void init(){
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
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    Trip callCreateTrip(User user){
        return tripRepository.save(Trip.builder()
                .name("Test Trip")
                .creator(user)
                .stops(new ArrayList<>())
                .adventureLevel(AdventureLevel.HIGH)
                .startDate(LocalDate.of(2022,1,1))
                .endDate(LocalDate.of(2023,1,10))
                .build());
    }

    void callStartRecommendationRequest(Long tripID) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Double radius = 4000.;
        Set<String> frontendCategories = Set.of();
        List<List<Double>> route;
        Integer limit = 50;

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("routes/routeLong.json").getFile());
        route = mapper.readValue(file, new TypeReference<>() {});

        recommendationService.startRecommendationRequests(tripID, radius, frontendCategories, route, limit);
    }

    @Test
    @DisplayName("start recommendation requests test 200")
    void testStartRecommendationRequests_200() throws IOException {
        Trip t = callCreateTrip(userService.user());
        callStartRecommendationRequest(t.getId());
        //Implicit success
    }

    @Test
    @DisplayName("start recommendation requests test 403")
    void testStartRecommendationRequests_403() throws IOException{
        User otherUser = userRepository.save(User.builder()
                .name("Test User 2")
                .emailAddress("test2@test.com")
                .username("tester2")
                .build());
        Trip t = callCreateTrip(otherUser);
        assertThrows(ForbiddenException.class, () -> callStartRecommendationRequest(t.getId()));
    }

    @Test
    @DisplayName("start recommendation requests test 404")
    void testStartRecommendationRequests_404() throws IOException{
        Long invalidID = -1L;
        assertThrows(NoSuchElementException.class, () -> callStartRecommendationRequest(invalidID));
    }

    @Test
    @DisplayName("get recommended locations test 200")
    @Sql(scripts= "/sql/categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetRecommendedLocations() throws IOException, InterruptedException {
        Trip t = callCreateTrip(userService.user());
        callStartRecommendationRequest(t.getId());


        long startTime = System.currentTimeMillis();
        RecommendationResponse recommendationResponse;
        Boolean hasStarted = false;
        Boolean hasFirstResults = false;
        do {
            recommendationResponse = recommendationService.getRecommendedLocations();
            if (!recommendationResponse.getIsDone()) {
                hasStarted = true;
            }
            log.debug("Rec response: " + recommendationResponse);
            sleep(100);
            if (!hasFirstResults && recommendationResponse.getLocations().size() > 0) {
                log.info("First results time: " + (System.currentTimeMillis() - startTime));
                hasFirstResults = true;
            }
        } while (!recommendationResponse.getIsDone() || !hasStarted);
        long endTime = System.currentTimeMillis();

        log.info("Time: " + (endTime - startTime));
        log.info("Size: " + recommendationResponse.getLocations().size());
    }
}
