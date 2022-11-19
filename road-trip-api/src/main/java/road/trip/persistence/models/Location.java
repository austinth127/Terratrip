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
@Table(name = Location.TABLE_NAME)
@NoArgsConstructor
@Builder
public class Location {
    public static final String TABLE_NAME = "LOCATION";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(length = 500)
    private String description;
    private String phoneContact;
    private String website;
    private String address;
    private Double rating;

    private double coordX;
    private double coordY;

    private String mapboxId;        // mapbox id
    private String geoapifyId;      // geoapify id
    private String otmId;           // open trip map id

    private String categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Location location = (Location) o;
        return (id != null && Objects.equals(id, location.id)) ||
            (geoapifyId != null && Objects.equals(geoapifyId, location.geoapifyId)) ||
            (mapboxId != null && Objects.equals(mapboxId, location.mapboxId)) ||
            (otmId != null && Objects.equals(otmId, location.otmId));
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
