package road.trip.api.category.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Category;
import road.trip.persistence.models.PlacesAPI;

import javax.persistence.Column;
import java.time.Month;

@Data
public class CategoryRequest {
    private String name;
    @JsonProperty("season_start")
    private Month seasonStart;
    @JsonProperty("season_end")
    private Month seasonEnd;
    @JsonProperty("adventure_level")
    private AdventureLevel adventureLevel;
    private PlacesAPI api;

    public Category buildCategory() {
        return Category.builder()
            .uiName(name)
            .seasonStart(seasonStart)
            .seasonEnd(seasonEnd)
            .api(api)
            .adventureLevel(adventureLevel)
            .build();
    }
}
