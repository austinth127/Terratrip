package road.trip.persistence.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date; //TODO: should I import the sql date instead?
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

    int adventureLevel; //TODO: Should this be an int?
    Date startDate;
    //End Date calculated based on route duration
    @ManyToOne()
    Stop start, end; //TODO: gives me an error

    @ManyToMany() //May need an annotation inside ()
    List<Stop> stops = new ArrayList<>();
    int duration; //hours
    double distance; //miles

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
