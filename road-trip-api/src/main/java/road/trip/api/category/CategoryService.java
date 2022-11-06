package road.trip.api.category;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.category.request.CategoryRequest;
import road.trip.api.category.response.CategoryResponse;
import road.trip.persistence.daos.CategoryRepository;
import road.trip.persistence.models.Category;
import road.trip.persistence.models.PlacesAPI;
import road.trip.persistence.models.Trip;
import road.trip.util.exceptions.NotFoundException;

import java.util.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import road.trip.util.UtilityFunctions;

import static road.trip.util.UtilityFunctions.fallsWithinTimeframe;

import static java.util.stream.Collectors.groupingBy;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    public List<CategoryResponse> getCategoryResponses() {
        List<CategoryResponse> categories = new ArrayList<>();

        getCategories().stream()
            .map(c -> c.getName().split("\\."))
            .collect(groupingBy(c -> c[0]))
            .forEach((c, subs) -> categories.add(new CategoryResponse(c, subs.stream()
                .map(sub -> sub[1])
                .collect(Collectors.toList())))
            );

        return categories;
    }

    public void addCategory(CategoryRequest request) {
        Category category = request.buildCategory();
        categoryRepository.save(category);
    }

    /**
     * Returns two lists of categories to be displayed on the frontend.
     * The first list contains the recommended ones which the user might be more interested in.
     * The second contains the rest of the categories.
     */
    public List<List<String>> getFilterCategories(Trip t){
        List<String> primary = getRecommendedCategories(t);
        List<String> secondary = getCategories().stream().map(Category::getName).collect(Collectors.toList());
        secondary.remove(primary);
        return List.of(primary, secondary);
    }

    /**
     * Returns a list of category names which the user might be interested in.
     */
    public List<String> getRecommendedCategories(Trip t){
        List<String> recommendedCategories = new ArrayList<>();
        List<Category> categories = getCategories();

        for(Category c : categories){
            //Category is of appropriate adventure level
            if(c.getAdventureLevel().ordinal() <= t.getAdventureLevel().ordinal()){
                //If category is year round
                if(c.getSeasonStart() == null || c.getSeasonEnd() == null){
                    recommendedCategories.add(c.getApiCategories());
                } else if( fallsWithinTimeframe(c.getSeasonStart(), c.getSeasonEnd(), t.getStartDate(), t.getEndDate()) ){
                    recommendedCategories.add(c.getApiCategories());
                }
            }
        }
        return recommendedCategories;
    }

    public Category getCategory(String categoryName) {
        Optional<Category> optCategory = categoryRepository.findByName(categoryName);
        if (optCategory.isEmpty()) {
            throw new NotFoundException(categoryName + " category not found in database");
        }
        return optCategory.get();
    }

    /*Given a list of categories formatted for the frontend, generate a list of categories that the api can interpret*/
    public List<String> getCategoriesByApi(List<String> frontendCategories, PlacesAPI api){
        List<Optional<Category>> categories = new ArrayList<>();
        List<String> apiCategories = new ArrayList<>();
        for(String str : frontendCategories){
            categories.add(categoryRepository.findByName(str));
        }
        for(Optional<Category> c : categories){
            if(c.isPresent() && c.get().getApi() == api){
                apiCategories.add(c.get().getApiCategories());
            }
        }
        return apiCategories;
    }

}
