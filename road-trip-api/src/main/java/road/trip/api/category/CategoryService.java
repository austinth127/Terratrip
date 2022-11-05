package road.trip.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.category.request.CategoryRequest;
import road.trip.persistence.daos.CategoryRepository;
import road.trip.persistence.models.Category;
import road.trip.persistence.models.Trip;
import road.trip.util.exceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
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
                    recommendedCategories.add(c.getName());
                } else if( false ){ //TODO Check to see if category timeframe overlaps trip timeframe
                    recommendedCategories.add(c.getName());
                }
            }
        }

        return recommendedCategories;
    }

    public Category getCategory(String categoryName) throws NotFoundException {
        Optional<Category> optCategory = categoryRepository.findByName(categoryName);
        if (optCategory.isEmpty()) {
            throw new NotFoundException(categoryName + " category not found in database");
        }
        return optCategory.get();
    }
}
