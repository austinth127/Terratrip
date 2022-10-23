package road.trip.clients;

import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import road.trip.clients.geoapify.GeoApifyClient;
import road.trip.clients.geoapify.LocationMapper;
import road.trip.persistence.models.Location;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Client Tests")
public class ClientTests {

    private final GeoApifyClient geoApifyClient = new GeoApifyClient(new LocationMapper());

    @Nested
    @DisplayName("GeoApifyClient Tests")
    class GeoApifyClientTests{
        @Test
        @DisplayName("getRecommendedStops")
        @Ignore
        void getRecommendedStops(){

            List<Location> locationList = geoApifyClient.getRecommendedStops(-105.0, 38, -93, 23);

            assertNotNull(locationList);
            assertNotEquals(locationList.size(), 0);
            for(Location l : locationList){
                assertNotNull(l); //Each object is valid
            }

        }

    }
}
