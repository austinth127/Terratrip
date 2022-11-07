package road.trip.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.category.request.CategoryRequest;
import road.trip.api.category.response.CategoryResponse;
import road.trip.persistence.models.Trip;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/category")
public class CategoryController {

    final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok(categoryService.getCategoryResponses());
    }

    /** TODO: ADMIN ONLY */
    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest request) {
        categoryService.addCategory(request);
        return ResponseEntity.ok().build();
    }

    /*
    @GetMapping
    public ResponseEntity<List<List<String>>> getFilterCategories(Trip t){
        List<List<String>> filterCategories = categoryService.getFilterCategories(t);
        return
    }
     */

}
