package road.trip.api.location;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import road.trip.api.category.CategoryService;
import road.trip.api.location.response.LocationResponse;
import road.trip.api.location.response.RecommendationResponse;
import road.trip.api.user.UserService;
import road.trip.clients.LocationRecommendationClient;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Trip;
import road.trip.util.ThrottledThreadPoolExecutor;
import road.trip.util.exceptions.ForbiddenException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.currentTimeMillis;
import static road.trip.persistence.models.PlacesAPI.GEOAPIFY;
import static road.trip.persistence.models.PlacesAPI.OPENTRIPMAP;
import static road.trip.util.UtilityFunctions.generateRefinedRoute;

@Service
@Log4j2
public class RecommendationService {

    private final LocationRecommendationClient geoApifyClient;
    private final LocationRecommendationClient otmClient;
    private final CategoryService categoryService;
    private final UserService userService;
    private final TripRepository tripRepository;
    private final ConcurrentHashMap<Long, ConcurrentHashMap<String, LocationResponse>> recommendationsByUser = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Boolean> isDoneByUser = new ConcurrentHashMap<>();

    public RecommendationService(@Qualifier("geoapify") LocationRecommendationClient geoApifyClient,
                                 @Qualifier("otm") LocationRecommendationClient otmClient,
                                 UserService userService, CategoryService categoryService,
                                 TripRepository tripRepository) {
        this.geoApifyClient = geoApifyClient;
        this.otmClient = otmClient;
        this.userService = userService;
        this.categoryService = categoryService;
        this.tripRepository = tripRepository;
    }

    public void startRecommendationRequests(Long tripId, Double radius, Set<String> frontendCategories, List<List<Double>> route, Integer limit) {
        Trip trip = tripRepository.findById(tripId).orElseThrow();
        if (trip.getCreator() != userService.user()) {
            throw new ForbiddenException("User does not own trip " + tripId);
        }
        Long userId = userService.getId();
        new Thread(() -> {
            try {
                log.info("Getting recommendations..");
                // Setup
                ThrottledThreadPoolExecutor geoApifyExecutor = new ThrottledThreadPoolExecutor(5);
                ThrottledThreadPoolExecutor otmExecutor = new ThrottledThreadPoolExecutor(4.8);
                setDone(userId, false);
                clearRecommendationCache(userId);
                List<List<Double>> refinedRoute = generateRefinedRoute(route, radius);

                // Recommend categories for the trip if needed
                Set<String> recommendedCategories;
                if (frontendCategories.isEmpty()) {
                    recommendedCategories = categoryService.getRecommendedCategories(trip);
                } else {
                    recommendedCategories = frontendCategories;
                }

                // Convert the frontend/recommended categories to API categories
                Set<String> geoApifyCategories = categoryService.getApiCategories(recommendedCategories, GEOAPIFY);
                Set<String> otmCategories = categoryService.getApiCategories(recommendedCategories, OPENTRIPMAP);

                // Get recommendation info without details
                getReducedRecommendationsAsync(userId, geoApifyExecutor, geoApifyClient, radius, geoApifyCategories, refinedRoute, limit);
                getReducedRecommendationsAsync(userId, otmExecutor, otmClient, radius, otmCategories, refinedRoute, limit);
                // Wait for threads to finish
                otmExecutor.joinAll();
                geoApifyExecutor.joinAll();
                log.info("Got All Reduced Recommendations");

                // Setup
                List<LocationResponse> recommendations = getRecommendationCache(userId).values().stream().toList();
                clearRecommendationCache(userId);

                // Get the recommendation details
                recommendations.stream()
                    .filter(r -> r.getOtmId() != null)
                    .sorted()
                    .limit(limit)
                    .forEach(recommendation -> {
                        addToRecommendationCache(userId, recommendation);
                        if (recommendation.getGeoapifyId() != null)
                            getDetailedRecommendationsAsync(userId, geoApifyExecutor, geoApifyClient, recommendation);
                        if (recommendation.getOtmId() != null)
                            getDetailedRecommendationsAsync(userId, otmExecutor, otmClient, recommendation);
                    });

                // Wait for threads to finish
                otmExecutor.joinAll();
                geoApifyExecutor.joinAll();
                setDone(userId, true);
                log.info("Got All Detailed Recommendations");
            } catch (InterruptedException e) {
                log.error(e);
            }
        }).start();
    }

    public RecommendationResponse getRecommendedLocations(Integer limit) {
        Long userId = userService.getId();
        List<LocationResponse> locations = getRecommendationCache(userId).values().stream().toList();

        return RecommendationResponse.builder()
            .locations(locations.stream()
                .sorted()
                .limit(limit)
                .collect(Collectors.toList()))
            .isDone(getDone(userId))
            .build();
    }

    /** HELPER FUNCTIONS */

    private void getDetailedRecommendationsAsync(Long userId, ThrottledThreadPoolExecutor executor,
                                                 LocationRecommendationClient client,
                                                 LocationResponse recommendation) {
        executor.execute(() -> addToRecommendationCache(userId, client.getLocationDetails(recommendation)));
    }

    private void getReducedRecommendationsAsync(Long userId, ThrottledThreadPoolExecutor executor,
                                                LocationRecommendationClient client, Double radius,
                                                Set<String> apiCategories,
                                                List<List<Double>> route, Integer limit) {
        for (List<Double> point : route) {
            executor.execute(() -> client.getRecommendedLocations(point.get(0), point.get(1), radius, apiCategories, limit)
                    .forEach(recommendation -> addToRecommendationCache(userId, recommendation))
            );
        }
    }

    private String bestString(String a, String b) {
        return a != null && !a.isBlank() ? a : b;
    }

    private <T> List<T> combineLists(List<T> a, List<T> b) {
        Set<T> combined = new HashSet<>();
        if (a != null) combined.addAll(a);
        if (b != null) combined.addAll(b);
        return new ArrayList<>(combined);
    }

    private LocationResponse combineRecommendations(LocationResponse a, LocationResponse b) {
        if (a.getCenter() != null && b.getCenter() != null) {
            String aid = a.getGeoapifyId() != null ? a.getGeoapifyId() : a.getOtmId();
            String bid = b.getGeoapifyId() != null ? b.getGeoapifyId() : b.getOtmId();
            log.debug("Combining {} with {}", aid, bid);
        }
        return LocationResponse.builder()
            .name(bestString(a.getName(), b.getName()))
            .center(a.getCenter())
            .description(bestString(a.getDescription(), b.getDescription()))
            .phoneContact(bestString(a.getPhoneContact(), b.getPhoneContact()))
            .website(bestString(a.getWebsite(), b.getWebsite()))
            .image(bestString(a.getImage(), b.getImage()))
            .address(bestString(a.getAddress(), b.getAddress()))
            .rating(a.getRating() != null ? a.getRating() : b.getRating())
            .userRating(a.getUserRating() != null ? a.getUserRating() : b.getUserRating())
            .categories(combineLists(a.getCategories(), b.getCategories()))
            .otmId(bestString(a.getOtmId(), b.getOtmId()))
            .osmId(a.getOsmId() != null ? a.getOsmId() : b.getOsmId())
            .geoapifyId(bestString(a.getGeoapifyId(), b.getGeoapifyId()))
            .build();
    }

    private void setDone(Long userId, Boolean val) {
        isDoneByUser.put(userId, val);
    }

    private Boolean getDone(Long userId) {
        return isDoneByUser.getOrDefault(userId, true);
    }

    private void clearRecommendationCache(Long userId) {
        setRecommendationCache(userId, new ConcurrentHashMap<>());
    }

    private void setRecommendationCache(Long userId, ConcurrentHashMap<String, LocationResponse> recommendations) {
        recommendationsByUser.put(userId, recommendations);
    }

    private void addToRecommendationCache(Long userId, LocationResponse recommendation) {
        String id;
        if (recommendation.getOsmId() != null)
            id = recommendation.getOsmId().toString();
        else if (recommendation.getOtmId() != null)
            id = recommendation.getOtmId();
        else
            id = recommendation.getGeoapifyId();

        ConcurrentHashMap<String, LocationResponse> recommendations = getRecommendationCache(userId);
        LocationResponse prevRecommendation = recommendations.getOrDefault(id, LocationResponse.builder().build());
        recommendation = combineRecommendations(prevRecommendation, recommendation);
        recommendations.put(id, recommendation);
        setRecommendationCache(userId, recommendations);
    }

    private ConcurrentHashMap<String, LocationResponse> getRecommendationCache(Long userId) {
        return recommendationsByUser.getOrDefault(userId, new ConcurrentHashMap<>());
    }

}
