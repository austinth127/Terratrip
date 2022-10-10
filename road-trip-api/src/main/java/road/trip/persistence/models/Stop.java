package road.trip.persistence.models;


import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long stopId;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    Trip trip;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @Column
    Integer order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Stop stop = (Stop) o;
        return stopId != null && Objects.equals(stopId, stop.stopId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
