package road.trip.api.location;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import road.trip.api.category.CategoryService;
import road.trip.api.location.response.LocationResponse;
import road.trip.api.location.response.RecommendationResponse;
import road.trip.api.user.UserService;
import road.trip.clients.LocationRecommendationClient;
import road.trip.clients.wikidata.WikidataClient;
import road.trip.persistence.daos.LocationRatingRepository;
import road.trip.persistence.daos.LocationRepository;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Trip;
import road.trip.util.ThrottledThreadPoolExecutor;
import road.trip.util.exceptions.ForbiddenException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.System.currentTimeMillis;
import static road.trip.persistence.models.PlacesAPI.GEOAPIFY;
import static road.trip.persistence.models.PlacesAPI.OPENTRIPMAP;
import static road.trip.util.UtilityFunctions.generateRefinedRoute;
import static road.trip.util.UtilityFunctions.reducedRoute;

@Service
@Log4j2
public class RecommendationService {

    public final static int RATING_WEIGHT = 2;
    public final static int ADVENTURE_WEIGHT = 2;
    public final static int DATA_WEIGHT = 1;

    private final LocationRecommendationClient geoApifyClient;
    private final LocationRecommendationClient otmClient;
    private final WikidataClient wikidataClient;
    private final CategoryService categoryService;
    private final UserService userService;
    private final TripRepository tripRepository;
    private final LocationRatingRepository locationRatingRepository;
    private final LocationRepository locationRepository;
    private final ConcurrentHashMap<Long, ConcurrentHashMap<String, LocationResponse>> recommendationsByUser = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Boolean> isDoneByUser = new ConcurrentHashMap<>();

    public RecommendationService(@Qualifier("geoapify") LocationRecommendationClient geoApifyClient,
                                 @Qualifier("otm") LocationRecommendationClient otmClient,
                                 WikidataClient wikidataClient,
                                 UserService userService, CategoryService categoryService,
                                 TripRepository tripRepository, LocationRatingRepository locationRatingRepository, LocationRepository locationRepository) {
        this.geoApifyClient = geoApifyClient;
        this.otmClient = otmClient;
        this.wikidataClient = wikidataClient;
        this.userService = userService;
        this.categoryService = categoryService;
        this.tripRepository = tripRepository;
        this.locationRatingRepository = locationRatingRepository;
        this.locationRepository = locationRepository;
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
                ThrottledThreadPoolExecutor wikidataExecutor = new ThrottledThreadPoolExecutor(40);
                setDone(userId, false);
                clearRecommendationCache(userId);
                List<List<Double>> refinedRoute = reducedRoute(route, limit / 2);

                log.info(refinedRoute.size());

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
                getReducedRecommendationsAsync(userId, geoApifyExecutor, geoApifyClient, radius, geoApifyCategories, refinedRoute, 2);
                getReducedRecommendationsAsync(userId, otmExecutor, otmClient, radius, otmCategories, refinedRoute, 2);
                // Wait for threads to finish
                otmExecutor.joinAll();
                geoApifyExecutor.joinAll();
                log.info("Got All Reduced Recommendations");

                // Setup
                List<LocationResponse> recommendations = getRecommendationCache(userId).values().stream().toList();
                clearRecommendationCache(userId);

                setRecommendationScore(recommendations, tripId);

                // Get the recommendation details
                recommendations.stream()
                    .sorted(Comparator.reverseOrder())
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
                log.info("Got All Detailed Recommendations");

                // Get WikiData images
                getRecommendationCache(userId).values().stream()
                    .filter(r -> r.getImage() == null || r.getImage().isBlank())
                    .filter(r -> r.getWikidataId() != null)
                    .forEach(recommendation ->
                        getWikidataAsync(userId, wikidataExecutor, wikidataClient, recommendation)
                    );
                wikidataExecutor.joinAll();
                log.info("Got All WikiData information");

                setDone(userId, true);
            } catch (Exception e) {
                log.error(e);
            }
        }).start();
    }

    public RecommendationResponse getRecommendedLocations(Integer limit, long tripId) {
        Long userId = userService.getId();
        List<LocationResponse> locations = getRecommendationCache(userId).values().stream().toList();

        setRecommendationScore(locations, tripId);

        return RecommendationResponse.builder()
            .locations(locations.stream()
                .sorted(Comparator.reverseOrder())
                .limit(limit)
                .collect(Collectors.toList()))
            .isDone(getDone(userId))
            .build();
    }

    /** HELPER FUNCTIONS */

    private void setRecommendationScore(List<LocationResponse> locations, long tripId) {
        int advLevel = tripRepository.findById(tripId).orElseThrow().getAdventureLevel().ordinal();

        locations.stream().forEach((loc) -> {
            long numRaings = locationRatingRepository.countAllByLocation(locationRepository.findById(loc.getId()).orElseThrow());

            loc.setRecommendationScore(calculateRecommendedScore(getRatingScore(loc.getRating(), numRaings),
                getAdventureScore(loc.getAdventureLevel().ordinal(), advLevel),
                getDataScore(loc)));
        });

    }

    private double calculateRecommendedScore(double ratingScore, double adventureScore, double dataScore) {
        return (ratingScore * RATING_WEIGHT) + (adventureScore * ADVENTURE_WEIGHT) + (dataScore * DATA_WEIGHT);

    }

    private double getRatingScore(double rating, long numRatings) {
        if(numRatings == 0) {
            return .5;
        }
        return ( (Math.pow(rating, 2)/10.0) + (5.0 * (1.0 - Math.pow(Math.exp(1), -.5*numRatings)) ) ) / 7.5;
    }

    private double getAdventureScore(int locationAdventureLevel, int tripAdventureLevel) {
        return (locationAdventureLevel + 1)/((double)(tripAdventureLevel + 1));
    }

    private double getDataScore(LocationResponse loc) {
        double score = 0;

        if (loc.getName() != null)
            score += .3;
        if(loc.getDescription() != null)
            score += .2;
        if(loc.getPhoneContact() != null)
            score += .05;
        if(loc.getWebsite() != null)
            score += .15;
        if(loc.getImage() != null)
            score += .15;
        if(loc.getAddress() != null)
            score += .15;

        return score;
    }

    private void getWikidataAsync(Long userId, ThrottledThreadPoolExecutor executor,
                                  WikidataClient client, LocationResponse location) {
        executor.execute(() -> {
            String imageUrl = client.getImageUrlFromWikidataId(location.getWikidataId());
            location.setImage(imageUrl);
            addToRecommendationCache(userId, location);
        });
    }

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
        else if (recommendation.getWikidataId() != null)
            id = recommendation.getWikidataId();
        else if (recommendation.getOtmId() != null)
            id = recommendation.getOtmId();
        else
            id = recommendation.getGeoapifyId();

        ConcurrentHashMap<String, LocationResponse> recommendations = getRecommendationCache(userId);
        LocationResponse prevRecommendation = recommendations.getOrDefault(id, LocationResponse.builder().build());
        recommendation = LocationResponse.combineRecommendations(prevRecommendation, recommendation);
        recommendations.put(id, recommendation);
        setRecommendationCache(userId, recommendations);
    }

    private ConcurrentHashMap<String, LocationResponse> getRecommendationCache(Long userId) {
        return recommendationsByUser.getOrDefault(userId, new ConcurrentHashMap<>());
    }

}
