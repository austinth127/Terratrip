package road.trip.persistence.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@Table(name = Trip.TABLE_NAME)
@NoArgsConstructor
@Builder
public class Trip {
    public static final String TABLE_NAME = "TRIP";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    String name;
    AdventureLevel adventureLevel;
    int duration;
    double distance;

    LocalDate startDate;
    LocalDate endDate;

    @OneToMany(mappedBy = "trip")
    List<Stop> stops;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Trip trip = (Trip) o;
        return id != null && Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
