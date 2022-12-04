package road.trip.clients;

import road.trip.api.location.response.LocationResponse;

import java.util.Set;

public interface LocationRecommendationClient {
    Set<LocationResponse> getRecommendedLocations(Double lat, Double lon, Double radius, Set<String> categories, Integer limit);
    LocationResponse getLocationDetails(LocationResponse location);
}
