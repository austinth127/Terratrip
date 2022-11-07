package road.trip.api.category.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import road.trip.persistence.models.Category;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class CategoryResponse {
    private String category;
    private List<String> subcategories;
}
