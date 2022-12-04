package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.AdventureLevel;
import road.trip.persistence.models.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByName(String categoryName);
    Optional<Category> findByName(String categoryName);
    List<Category> findAllByUseByDefault(Boolean useByDefault);
}
