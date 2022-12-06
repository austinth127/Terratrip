package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Category;
import road.trip.persistence.models.PlacesAPI;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUiName(String uiName);
    Optional<Category> findFirstByUiNameAndApi(String categoryName, PlacesAPI api);
    List<Category> findAllByUseByDefault(Boolean useByDefault);
    List<Category> findAllByApiCategoriesContaining(String query);
}
