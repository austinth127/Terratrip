package road.trip.clients.geoapify;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import road.trip.api.location.response.LocationResponse;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Log4j2
public class GeoApifyClientTests {
    @Autowired GeoApifyClient client;

    @Test
    void testGetRecommendedLocations() {
        final int limit = 15;

        // Colorado ski search
        Set<LocationResponse> responses = client.getRecommendedLocations(-104.8758, 39.762, 140000.0, Set.of("ski.lift"), limit);
        responses.forEach(r -> {
            assertNotNull(r);
            assertNotNull(r.getAddress());
            assertNotNull(r.getCenter());
            assertNotNull(r.getCategories());
            assertNotNull(r.getGeoapifyId());
        });
        assertEquals(limit, responses.size());
    }

    /**
     * Disabled to avoid making a bunch of API calls. Feel free to re-enable if needed :)
     */
    @Test
    @Disabled
    void testParallelism() throws InterruptedException {
        final int limit = 15;
        final int numRequests = 25;

        // Make the requests
        for (int i = 0; i < numRequests; i++) {
            client.getRecommendedLocationsAsync(-104.8758, 39.762, 140000.0, Set.of("ski.lift"), limit);
        }

        // Get the results
        Set<LocationResponse> locations = client.executeAsync();

        // Verify the results
        locations.forEach(l -> {
            assertNotNull(l);
            assertNotNull(l.getAddress());
            assertNotNull(l.getCenter());
            assertNotNull(l.getCategories());
            assertNotNull(l.getGeoapifyId());
        });
        assertEquals(limit, locations.size());
    }

}
