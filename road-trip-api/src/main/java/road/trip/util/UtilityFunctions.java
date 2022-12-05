package road.trip.util;

import java.io.IOException;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import org.springframework.boot.SpringApplication;
import road.trip.RoadTripApplication;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import road.trip.util.exceptions.BadRequestException;
import road.trip.util.exceptions.TooManyRequestsException;
import road.trip.util.exceptions.UnauthorizedException;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.lang.Math;

public class UtilityFunctions {

    final static Double METERS_IN_A_DEGREE = 111139.0;

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

        switch (httpResponse.statusCode()) {
            case 400:
                throw new BadRequestException(httpResponse.body());
            case 401:
                throw new UnauthorizedException();
            case 429:
                throw new TooManyRequestsException();
        }

        return httpResponse.body();
    }

    public static String bestString(String a, String b) {
        return a != null && !a.isBlank() ? a : b;
    }

    public static <T> List<T> combineLists(List<T> a, List<T> b) {
        Set<T> combined = new HashSet<>();
        if (a != null) combined.addAll(a);
        if (b != null) combined.addAll(b);
        return new ArrayList<>(combined);
    }

    public static List<List<Double>> generateRefinedRoute(List<List<Double>> route, Double radius){
        final Double TARGET_DISTANCE_BETWEEN_POINTS = radius;

        Double distanceToNextRefinedPoint = TARGET_DISTANCE_BETWEEN_POINTS;
        Queue<List<Double>> points = new LinkedList<>(route);
        List<List<Double>> refinedRoute = new ArrayList<>();
        Double distanceBetweenPoints;
        List<Double> cur, next;

        // Add start to refined route
        cur = route.get(0);
        refinedRoute.add(cur);

        while (!points.isEmpty()) {
            next = points.peek();
            distanceBetweenPoints = calcDistanceLatitudeLongitude(cur.get(0), cur.get(1), next.get(0), next.get(1));
            if (distanceToNextRefinedPoint > distanceBetweenPoints) {
                cur = next;
                points.remove();
                distanceToNextRefinedPoint -= distanceBetweenPoints;
            } else {
                cur = calcPointAlongLine(cur.get(0), cur.get(1), next.get(0), next.get(1), distanceToNextRefinedPoint);
                distanceToNextRefinedPoint = TARGET_DISTANCE_BETWEEN_POINTS;
                refinedRoute.add(cur);
            }
        }

        // Add end to refined route
        refinedRoute.add(route.get(route.size() - 1));

        return refinedRoute;
    }

    public static double calcDistanceLatitudeLongitude(double lon1, double lat1, double lon2, double lat2) {
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r) * 1000; // multiplied for km -> m
    }

    private static List<Double> calcPointAlongLine(Double x1, Double y1, Double x2, Double y2, Double distanceToNextRefinedPoint){
        Vector2D p1 = new Vector2D(x1, y1);
        Vector2D p2 = new Vector2D(x2, y2);
        Vector2D v = p2;
        v = v.subtract(p1);
        //System.out.println("Original V: " + v.getX() + " " + v.getY());

        v = v.normalize();
        //System.out.println("Normalize V: " + v.getX() + " " + v.getY());

        v = v.scalarMultiply(distanceToNextRefinedPoint / METERS_IN_A_DEGREE);
        //System.out.println("Scaled V: " + v.getX() + " " + v.getY());
        v = v.add(p1);
        //System.out.println("Final V: " + v.getX() + " " + v.getY());

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
