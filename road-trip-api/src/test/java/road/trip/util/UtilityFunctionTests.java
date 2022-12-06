package road.trip.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.DataOutput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static road.trip.util.UtilityFunctions.*;

@SpringBootTest
@Log4j2
public class UtilityFunctionTests {

    @Test
    void testCalculateDistanceBig() {
        List<Double> newYork = List.of(-73.9249, 40.6943), denver = List.of(-104.876, 39.762);
        Double distance;
        distance = calcDistanceLatitudeLongitude(newYork.get(0), newYork.get(1), denver.get(0), denver.get(1));

        System.out.println(distance/1000);
    }

    @Test
    void testCalculateDistanceSmall() {
        List<Double> walmart = List.of(-97.105984, 31.601604), magnolia = List.of(-97.129434362885, 31.552537697099652);
        Double distance;
        distance = calcDistanceLatitudeLongitude(walmart.get(0), walmart.get(1), magnolia.get(0), magnolia.get(1));

        System.out.println(distance);
    }

    @Test
    @Disabled
    void testGenerateRefinedRoute() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<List<Double>> route;
        List<List<Double>> refinedRoute;
        Double radius = 4000.;

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("route1.json").getFile());
        route = mapper.readValue(file, new TypeReference<>() {});


        refinedRoute = generateRefinedRoute(route, radius);
        assertNotEquals(0, refinedRoute.size());

        // Print the first 10 points
        refinedRoute.stream()
                .limit(10)
                .forEach(coord -> log.info("(" + coord.get(0) + " " + coord.get(1) + ")"));

        // Print the size
        log.info(refinedRoute.size());

        Double error = 500.; // meters
        for (int i = 0; i < refinedRoute.size() - 2; i++) {
            List<Double> cur = refinedRoute.get(i);
            List<Double> next = refinedRoute.get(i+1);
            Double distance = calcDistanceLatitudeLongitude(cur.get(0), cur.get(1), next.get(0), next.get(1));
            log.info("distance: " + distance);
            assertTrue(Math.abs(distance - radius) <= error);
        }
    }
}
