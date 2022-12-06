package road.trip.api.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.category.request.CategoryRequest;
import road.trip.api.category.response.CategoryResponse;
import road.trip.persistence.daos.CategoryRepository;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Category;
import road.trip.persistence.models.PlacesAPI;
import road.trip.persistence.models.Trip;

import java.util.*;
import java.util.stream.Collectors;

import static road.trip.util.UtilityFunctions.fallsWithinTimeframe;

import static java.util.stream.Collectors.groupingBy;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private List<Category> getDefaultCategories() {
        return categoryRepository.findAllByUseByDefault(true);
    }

    public List<CategoryResponse> getCategoryResponses(Integer a) {
        if (a < 0 || a > 3) return null;
        List<CategoryResponse> categories = new ArrayList<>();
        categoryRepository.findAll().stream()
            .filter(c -> c.getAdventureLevel().ordinal() <= a)
            .map(c -> c.getUiName().split("\\."))
            .collect(groupingBy(c -> c[0]))
            .forEach((c, subs) -> categories.add(new CategoryResponse(c, subs.stream()
                .map(sub -> sub[1])
                .distinct()
                .collect(Collectors.toList())))
            );

        return categories;
    }

    public AdventureLevel getAdventureLevel(List<String> categories) {
        AdventureLevel advLevel = AdventureLevel.EXTREME;
        boolean noMatches = true;

        for(int i = 0; i < categories.size(); i++) {
            List<Category> c = categoryRepository.findAllByUiName(categories.get(i));
            if(c.size() > 0) {
                if(advLevel.ordinal() > c.get(0).getAdventureLevel().ordinal()) {
                    advLevel = c.get(0).getAdventureLevel();
                }
                noMatches = false;
            }
        }
        return noMatches ? AdventureLevel.RELAXED : advLevel;
    }

    /**
     * Returns a list of frontend category names which the user might be interested in.
     */

    public Set<String> getRecommendedCategories(Trip trip) {
        return getDefaultCategories().stream()
            // Category is of appropriate adventure level
            .filter(category -> category.getAdventureLevel().ordinal() <= trip.getAdventureLevel().ordinal())
            // Category is of appropriate time of year
            .filter(category -> {
                if (category.getSeasonStart() == null || category.getSeasonEnd() == null) {
                    // Category is valid for all year
                    return true;
                }
                return fallsWithinTimeframe(category.getSeasonStart(), category.getSeasonEnd(), trip.getStartDate(), trip.getEndDate());
            })
            // Get the name of category that the frontend uses
            .map(Category::getUiName)
            // Combine results into a list
            .collect(Collectors.toSet());
    }

    public Set<String> getApiCategories(Set<String> uiNames, PlacesAPI api) {
        return uiNames.stream()
            // Get Category objects from the given ui names and API
            .map(uiName -> categoryRepository.findFirstByUiNameAndApi(uiName, api).orElse(null))
            // Ignore null results
            .filter(Objects::nonNull)
            // Get a comma-separated list of api category strings
            .map(Category::getApiCategories)
            // Split each list of api categories
            .flatMap(apiCategories -> Arrays.stream(apiCategories.split(",")))
            // Combine all results into a set to avoid duplicates
            .collect(Collectors.toSet());
    }

    public Set<String> getUiCategories(List<String> apiCategories) {
        return apiCategories.stream().map(categoryRepository::findAllByApiCategoriesContaining)
            .flatMap(Collection::stream)
            .map(Category::getUiName)
            .collect(Collectors.toSet());
    }
}
