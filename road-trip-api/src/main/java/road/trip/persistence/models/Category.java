package road.trip.persistence.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Month;
import java.util.Objects;

@Entity
@Getter @Setter @ToString @AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String apiCategories;
    private Month seasonStart;
    private Month seasonEnd;

    private AdventureLevel adventureLevel;

    private PlacesAPI api;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
