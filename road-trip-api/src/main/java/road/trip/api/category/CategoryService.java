package road.trip.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.category.request.CategoryRequest;
import road.trip.persistence.daos.CategoryRepository;
import road.trip.persistence.models.Category;

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
}
