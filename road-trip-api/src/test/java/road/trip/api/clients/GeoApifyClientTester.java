package road.trip.api.clients;

import org.junit.jupiter.api.Test;

public class GeoApifyClientTester {
    GeoApifyClient geoApifyClient = new GeoApifyClient();

    @Test
    void testGetStopByAddress(){
        String str = "";
        geoApifyClient.getStopByAddress(str);
    }
}
