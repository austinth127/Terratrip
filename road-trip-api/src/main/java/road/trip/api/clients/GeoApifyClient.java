package road.trip.api.clients;

import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GeoApifyClient {

    /*
    public List<Stop> getRecommendedStops(Trip t, Category c) {

    }
     */

    public Stop getStopByAddress(String address){
        HttpURLConnection http = null;
        try {
            URL url = new URL("https://api.geoapify.com/v2/places?categories=commercial.supermarket&filter=rect%3A10.716463143326969%2C48.755151258420966%2C10.835314015356737%2C48.680903341613316&limit=20&apiKey=a9b12a2a2ae0491cb7874bbf0fab7115");
            http = (HttpURLConnection)url.openConnection();
            http.setRequestProperty("Accept", "application/json");

            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            http.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Stop();
    }
}
