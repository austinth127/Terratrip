package road.trip.api.clients;

import org.junit.jupiter.api.Test;

public class GeoApifyClientTester {
    GeoApifyClient geoApifyClient = new GeoApifyClient();

    @Test
    void testGetStopByName(){
        String str = "";
        geoApifyClient.getStopByName(str);
    }
}
