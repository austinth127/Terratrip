package road.trip.api.category;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import road.trip.persistence.models.PlacesAPI;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
@Log4j2
public class CategoryServiceTests {

    @Autowired
    CategoryService categoryService;

    @Test
    void testGetCategoriesByApi_200(){

    }

    @Test
    void getRecommendedCategories_200(){

    }

    @Test
    public void getCategoriesByApiTest(){
        PlacesAPI api = PlacesAPI.GEOAPIFY;
        List<String> input1 = List.of("ski_and_snowboard.ski");
        List<String> input2 = List.of("leisure.picnic", "leisure.garden", "leisure.playground", "leisure.park", "leisure.nature_reserve");
        List<String> input3 = List.of("sport.dive", "shops.fishing", "leisure.park");
        Set<String> input1Set = new HashSet<>(), input2Set = new HashSet<>(), input3Set = new HashSet<>();

        Set<String> output1 = Set.of("ski");
        Set<String> output2 = Set.of("leisure.picnic", "leisure.park.garden", "leisure.playground", "leisure.park", "leisure.park.nature_reserve");
        Set<String> output3 = Set.of("sport.dive_centre", "commercial.outdoor_and_sport.fishing", "leisure.park");

        input1Set.addAll(categoryService.getCategoriesByApi(input1, api));
        input1Set.removeAll(output1);
        assertTrue(input1Set.isEmpty());

        input2Set.addAll(categoryService.getCategoriesByApi(input2, api));
        input2Set.removeAll(output2);
        assertTrue(input2Set.isEmpty());

        input3Set.addAll(categoryService.getCategoriesByApi(input3, api));
        input3Set.removeAll(output3);
        assertTrue(input3Set.isEmpty());

    }

}
