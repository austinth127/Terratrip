package road.trip.persistence.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@Table(name = Route.TABLE_NAME)
@NoArgsConstructor
@Builder
public class Route {

    public static final String TABLE_NAME = "ROUTE";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    //Stop start, end; TODO: gives me an error
    //List<Stop> stops = new ArrayList<>();
    int duration; //hours
    double distance; //miles

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
