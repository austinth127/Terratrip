package road.trip.util;

import java.io.IOException;
import java.net.http.HttpClient;
import org.springframework.boot.SpringApplication;
import road.trip.RoadTripApplication;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import road.trip.util.exceptions.TooManyRequestsException;
import road.trip.util.exceptions.UnauthorizedException;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.lang.Math;

public class UtilityFunctions {

    /**
     * Used to determine whether the time frame of a category overlaps with the time frame of a trip
     * @param categoryStartMonth
     * @param categoryEndMonth
     * @param tripStartDate
     * @param tripEndDate
     * @return
     */
    public static boolean fallsWithinTimeframe(Month categoryStartMonth, Month categoryEndMonth, LocalDate tripStartDate, LocalDate tripEndDate){
        Integer tripStart = tripStartDate.getMonthValue(), tripEnd = tripEndDate.getMonthValue();
        Integer categoryStart = categoryStartMonth.getValue(), categoryEnd = categoryEndMonth.getValue();
        Set<Integer> tripTimeFrame = new HashSet<>(), categoryTimeFrame = new HashSet<>();

        tripTimeFrame.add(tripStart);
        while(tripStart != tripEnd){
            tripStart = tripStart % 12 + 1;
            tripTimeFrame.add(tripStart);
        }
        categoryTimeFrame.add(categoryStart);
        while(categoryStart != categoryEnd){
            categoryStart = categoryStart % 12 + 1;
            categoryTimeFrame.add(categoryStart);
        }
        tripTimeFrame.retainAll(categoryTimeFrame);
        return !tripTimeFrame.isEmpty();
    }

    public static String doGet(HttpClient httpClient, URI uri) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(uri)
            .GET()
            .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 401) {
            throw new UnauthorizedException();
        }
        if (httpResponse.statusCode() == 429) {
            throw new TooManyRequestsException();
        }

        return httpResponse.body();
    }

    public static List<List<Double>> generateRefinedRoute(List<List<Double>> route, Double radius){
        Double curDistance = radius * 2;
        List<List<Double>> refinedRoute = new ArrayList<>();
        //Add start and end to refined route
        refinedRoute.add(route.get(0));
        refinedRoute.add(route.get(route.size() - 1));

        Double x1, x2, y1, y2, dist;

        //For each point except for the last one
        for(int i = 0; i < route.size() - 1; i++){
            x1 = route.get(i).get(0);
            y1 = route.get(i).get(1);
            x2 = route.get(i+1).get(0);
            y2 = route.get(i+1).get(1);
            dist = calcDist(x1, y1, x2, y2);
            while(dist >= curDistance){
                List<Double> newPoint = calcPointAlongLine(x1, y1, x2, y2, curDistance);
                refinedRoute.add(newPoint);
                x1 = newPoint.get(0);
                y1 = newPoint.get(1);
                dist = calcDist(x1, y1, x2, y2);
            }
            curDistance-=dist;
        }

        return refinedRoute;
    }

    private static Double calcDist(Double x1, Double y1, Double x2, Double y2){
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private static List<Double> calcPointAlongLine(Double x1, Double y1, Double x2, Double y2, Double dist){
        Vector2D p1 = new Vector2D(x1, y1);
        Vector2D p2 = new Vector2D(x2, y2);
        Vector2D v = p2;
        v = v.subtract(p1);
        System.out.println("Original V: " + v.getX() + " " + v.getY());

        v = v.normalize();
        System.out.println("Normalize V: " + v.getX() + " " + v.getY());

        v = v.scalarMultiply(dist);
        System.out.println("Scaled V: " + v.getX() + " " + v.getY());
        v = v.add(p1);
        System.out.println("Final V: " + v.getX() + " " + v.getY());

        return List.of(v.getX(), v.getY());
    }

    public static void main(String[] args) {
        /*
        Month categoryStart = Month.JUNE, categoryEnd = Month.JUNE;
        LocalDate tripStart = LocalDate.of(2020, 7, 7), tripEnd = LocalDate.of(2020, 7, 8);

        boolean intersects = fallsWithinTimeframe(categoryStart, categoryEnd, tripStart, tripEnd);
         */
        List<List<Double>> myList =  List.of(List.of(0d, 0d), List.of(0d, 5d), List.of(3d, 9d), List.of(5.5d, 9d), List.of(6.75d, 9d), List.of(8d, 9d));
        Double radius = 1.25d;

        List<List<Double>> point = generateRefinedRoute(myList, radius);
        System.out.println(point);
    }
}
