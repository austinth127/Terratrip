package road.trip.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.category.request.CategoryRequest;
import road.trip.api.category.response.CategoryResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/category")
public class CategoryController {

    final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> categories = categoryService.getCategories().stream()
            .map(CategoryResponse::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    /** TODO: ADMIN ONLY */
    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest request) {
        categoryService.addCategory(request);
        return ResponseEntity.ok().build();
    }

}