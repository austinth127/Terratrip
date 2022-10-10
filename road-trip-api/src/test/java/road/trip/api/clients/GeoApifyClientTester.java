package road.trip.api.clients;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import road.trip.api.clients.geoapify.GeoApifyClient;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GeoApifyClientTester {
    GeoApifyClient geoApifyClient;

    @Test
    void testGetStopByName(){
        String str = "";
        geoApifyClient.getStopByName(str);
    }
}
