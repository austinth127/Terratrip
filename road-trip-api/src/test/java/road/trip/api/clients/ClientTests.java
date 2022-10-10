package road.trip.api.clients;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import road.trip.api.clients.geoapify.GeoApifyClient;
import road.trip.api.clients.geoapify.LocationMapper;
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
        @DisplayName("getStopByName")
        void getStopByName(){
            String nameParam = "Walmart";

            List<Location> locationList = geoApifyClient.getStopByName(nameParam, 5, 35.5, -106, 27.3, -92.5);

            assertNotNull(locationList);
            for(Location l : locationList){
                assertNotNull(l); //Each object is valid
            }
        }

    }
}
