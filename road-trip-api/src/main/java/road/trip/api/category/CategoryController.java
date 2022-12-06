package road.trip.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.category.response.CategoryResponse;
import road.trip.persistence.models.AdventureLevel;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/category")
public class CategoryController {

    final CategoryService categoryService;

    @GetMapping("/{advLevel}")
    public ResponseEntity<List<CategoryResponse>> getCategories(@PathVariable("advLevel") Integer advLevel) {
        return ResponseEntity.ok(categoryService.getCategoryResponses(advLevel));
    }

}
