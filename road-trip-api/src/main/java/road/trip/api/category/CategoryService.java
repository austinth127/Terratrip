package road.trip.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.category.request.CategoryRequest;
import road.trip.persistence.daos.CategoryRepository;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Category;
import road.trip.persistence.models.Trip;

import java.util.ArrayList;
import java.util.List;

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
}
