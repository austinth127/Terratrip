package road.trip.api.location;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Location;
import road.trip.persistence.models.Trip;
import road.trip.util.ThrottledThreadPoolExecutor;
import road.trip.util.exceptions.ForbiddenException;

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
    public final static int MIN_RECS_PER_SEARCH = 2;

    private final LocationRecommendationClient geoApifyClient;
    private final LocationRecommendationClient otmClient;
    private final WikidataClient wikidataClient;
    private final CategoryService categoryService;
    private final UserService userService;
    private final TripRepository tripRepository;
    private final LocationRatingRepository locationRatingRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final ConcurrentHashMap<Long, ConcurrentHashMap<String, LocationResponse>> recommendationsByUser = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Boolean> isDoneByUser = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Integer> limitsByUser = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Long> tripIdsByUser = new ConcurrentHashMap<>();

    @Autowired
    public RecommendationService(@Qualifier("geoapify") LocationRecommendationClient geoApifyClient,
                                 @Qualifier("otm") LocationRecommendationClient otmClient,
                                 WikidataClient wikidataClient,
                                 UserService userService, CategoryService categoryService,
                                 LocationService locationService,
                                 TripRepository tripRepository, LocationRatingRepository locationRatingRepository, LocationRepository locationRepository) {
        this.geoApifyClient = geoApifyClient;
        this.otmClient = otmClient;
        this.wikidataClient = wikidataClient;
        this.userService = userService;
        this.categoryService = categoryService;
        this.tripRepository = tripRepository;
        this.locationRatingRepository = locationRatingRepository;
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }

    public void startRecommendationRequests(Long tripId, Double radius, Set<String> frontendCategories, List<List<Double>> route, Integer limit) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(); //404
        log.info(trip.getCreator());
        log.info(userService.user());
        if (!trip.getCreator().equals(userService.user())) {
            throw new ForbiddenException("User does not own trip " + tripId); //403
        }

        if(limit == null){
            log.error("limit is null");
            return;
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
                setTripId(userId, tripId);
                setLimit(userId, limit);
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
                int numPoints = refinedRoute.size();
                int numRecsPerSearch = Math.max(MIN_RECS_PER_SEARCH, numPoints == 0 ? 0 : limit / numPoints);
                getReducedRecommendationsAsync(userId, geoApifyExecutor, geoApifyClient, radius, geoApifyCategories, refinedRoute, numRecsPerSearch);
                getReducedRecommendationsAsync(userId, otmExecutor, otmClient, radius, otmCategories, refinedRoute, numRecsPerSearch);
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
                    .filter(l -> l.getCenter() != null)
                    .filter(l -> l.getName() != null && !l.getName().isBlank())
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
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }).start();
    }

    public RecommendationResponse getRecommendedLocations() {
        Long userId = userService.getId();
        Long tripId = getTripId(userId);
        Integer limit = getLimit(userId);
        Boolean isDone = getDone(userId);
        if (tripId == null || limit == null) {
            return RecommendationResponse.builder()
                .locations(new ArrayList<>())
                .isDone(isDone)
                .build();
        }

        List<LocationResponse> locations = getRecommendationCache(userId).values().stream().toList();
        setRecommendationScore(locations, tripId);

        return RecommendationResponse.builder()
            .locations(locations.stream()
                .filter(l -> l.getCenter() != null)
                .filter(l -> l.getName() != null && !l.getName().isBlank())
                .sorted(Comparator.reverseOrder())
                .limit(limit)
                .collect(Collectors.toList()))
            .isDone(isDone)
            .build();
    }

    /** HELPER FUNCTIONS */

    private void setRecommendationScore(List<LocationResponse> locations, long tripId) {
        int tripAdvLevel = tripRepository.findById(tripId).orElseThrow().getAdventureLevel().ordinal();

        locations.forEach(locationResponse -> {
            Location location = locationService.findLocationByIds(locationResponse).orElse(null);
            int numRatings = 0;
            AdventureLevel locAdvLevel = AdventureLevel.RELAXED;
            Double rating = null;
            if (location != null) {
                numRatings = locationService.getNumRatings(location);
                locAdvLevel = locationService.getAdventureLevel(location);
                rating = location.getRating();
            }
            locationResponse.setRecommendationScore(
                calculateRecommendedScore(getRatingScore(rating, numRatings),
                    getAdventureScore(locAdvLevel != null ? locAdvLevel.ordinal() : 0, tripAdvLevel),
                    getDataScore(locationResponse)));
        });
    }

    private double calculateRecommendedScore(double ratingScore, double adventureScore, double dataScore) {
        return (ratingScore * RATING_WEIGHT) + (adventureScore * ADVENTURE_WEIGHT) + (dataScore * DATA_WEIGHT);
    }

    private double getRatingScore(Double rating, long numRatings) {
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

        if(loc.getDescription() != null)
            score += .3;
        if(loc.getPhoneContact() != null)
            score += .10;
        if(loc.getWebsite() != null)
            score += .2;
        if(loc.getImage() != null)
            score += .2;
        if(loc.getAddress() != null)
            score += .2;

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

    private void setLimit(Long userId, Integer limit) {
        limitsByUser.put(userId, limit);
    }

    private Integer getLimit(Long userId) {
        return limitsByUser.get(userId);
    }

    private void setTripId(Long userId, Long tripId) {
        tripIdsByUser.put(userId, tripId);
    }

    private Long getTripId(Long userId) {
        return tripIdsByUser.get(userId);
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
