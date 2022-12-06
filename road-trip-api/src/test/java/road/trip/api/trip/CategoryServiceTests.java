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
import road.trip.api.category.CategoryService;
import road.trip.api.category.response.CategoryResponse;
import road.trip.api.trip.request.TripEditRequest;
import road.trip.api.trip.response.TripResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.CategoryRepository;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.daos.UserRepository;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Trip;
import road.trip.persistence.models.User;
import road.trip.util.exceptions.ForbiddenException;
import road.trip.util.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Log4j2
@ActiveProfiles("test")
@DisplayName("Category Service Tests")
public class CategoryServiceTests {
    @Autowired
    TripRepository tripRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TripService tripService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
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
    @DisplayName("Category Responses Test")
    @Disabled
    void getCategoryResponsesTest(){
        List<CategoryResponse> categories = categoryService.getCategoryResponses(3);
        assertTrue(categories.size()>0);

    }
    @Test
    @DisplayName("Get Recommended Categories Test")
    void getRecommendedCategoriesTest(){
        Trip trip = tripRepository.save(Trip.builder()
            .name("Test Trip")
            .creator(userService.user())
            .stops(new ArrayList<>())
            .adventureLevel(AdventureLevel.HIGH)
            .startDate(LocalDate.of(2022,1,1))
            .endDate(LocalDate.of(2023,1,10))
            .build());
        Set<String> categories = categoryService.getRecommendedCategories(trip);
        System.err.println(categories);


    }
    @Test
    @DisplayName("Get API Categories Test")
    void getAPICategoriesTest(){

    }
}