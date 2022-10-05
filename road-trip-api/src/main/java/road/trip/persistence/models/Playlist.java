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
@Table(name = Trip.TABLE_NAME)
@NoArgsConstructor
@Builder
public class Playlist {
    public static final String TABLE_NAME = "PLAYLIST";

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    String name, url;
    //Genre genre;
    long userId, tripId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Playlist playlist = (Playlist) o;
        return id != null && Objects.equals(id, playlist.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
