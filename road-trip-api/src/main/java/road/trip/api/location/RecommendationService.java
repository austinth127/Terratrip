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
import road.trip.persistence.models.User;
import road.trip.util.exceptions.ForbiddenException;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.MINUTES;
import static road.trip.persistence.models.PlacesAPI.GEOAPIFY;
import static road.trip.persistence.models.PlacesAPI.OPENTRIPMAP;
import static road.trip.util.UtilityFunctions.generateRefinedRoute;

@Service
@Log4j2
public class RecommendationService {

    private static final Integer THREAD_POOL_SIZE = 30;

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
                // Setup
                ExecutorService esReduced = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
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
                //getReducedRecommendationsAsync(userId, esReduced, geoApifyClient, radius, geoApifyCategories, refinedRoute, limit);
                getReducedRecommendationsAsync(userId, esReduced, otmClient, radius, otmCategories, refinedRoute, limit);
                // Wait for threads to finish
                esReduced.shutdown();
                esReduced.awaitTermination(10, MINUTES);

                // Setup
                ExecutorService esDetails = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
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
                            getDetailedRecommendationsAsync(userId, esDetails, geoApifyClient, recommendation);
                        if (recommendation.getOtmId() != null)
                            getDetailedRecommendationsAsync(userId, esDetails, otmClient, recommendation);
                    });

                // Wait for threads to finish
                esDetails.shutdown();
                esDetails.awaitTermination(10, MINUTES);
                setDone(userId, true);
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

    private void getDetailedRecommendationsAsync(Long userId, ExecutorService es,
                                                 LocationRecommendationClient client,
                                                 LocationResponse recommendation) {
        es.submit(() -> addToRecommendationCache(userId, client.getLocationDetails(recommendation)));
    }

    private void getReducedRecommendationsAsync(Long userId, ExecutorService es,
                                                LocationRecommendationClient client, Double radius,
                                                Set<String> apiCategories,
                                                List<List<Double>> route, Integer limit) {
        for (List<Double> point : route) {
            es.submit(() -> client.getRecommendedLocations(point.get(0), point.get(1), radius, apiCategories, limit)
                    .forEach(recommendation -> addToRecommendationCache(userId, recommendation))
            );
        }
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
        String id = recommendation.getGeoapifyId() == null ? recommendation.getOtmId() : recommendation.getGeoapifyId();
        ConcurrentHashMap<String, LocationResponse> recommendations = getRecommendationCache(userId);
        recommendations.put(id, recommendation);
        setRecommendationCache(userId, recommendations);
    }

    private ConcurrentHashMap<String, LocationResponse> getRecommendationCache(Long userId) {
        return recommendationsByUser.getOrDefault(userId, new ConcurrentHashMap<>());
    }

}
