package road.trip.persistence.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@Table(name = Stop.TABLE_NAME)
@NoArgsConstructor
@Builder
public class Stop {
    public static final String TABLE_NAME = "STOP";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    String name;
    String description;
    int rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Stop stop = (Stop) o;
        return id != null && Objects.equals(id, stop.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
