package road.trip.api.clients;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import road.trip.api.clients.geoapify.GeoApifyClient;

public class GeoApifyClientTester {
    final GeoApifyClient geoApifyClient;

    @Autowired
    GeoApifyClientTester(GeoApifyClient geoApifyClient){
        this.geoApifyClient = geoApifyClient;
    }

    @Test
    void testGetStopByName(){
        String str = "";
        geoApifyClient.getStopByName(str);
    }
}
