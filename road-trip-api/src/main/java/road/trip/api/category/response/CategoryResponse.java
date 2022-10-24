package road.trip.api.category.response;

import lombok.Data;
import road.trip.persistence.models.Category;

@Data
public class CategoryResponse {
    private String name;

    public CategoryResponse(Category category) {
        name = category.getName();
    }
}
