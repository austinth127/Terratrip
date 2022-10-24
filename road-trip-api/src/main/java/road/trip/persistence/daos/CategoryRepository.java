package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import road.trip.persistence.models.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
