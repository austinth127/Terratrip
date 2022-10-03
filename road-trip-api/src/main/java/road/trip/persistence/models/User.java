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
@Table(name = User.TABLE_NAME)
@NoArgsConstructor
@Builder
public class User {

    public static final String TABLE_NAME = "USER";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;

    Long userfrontId;
    String emailAddress;
    String name;
    String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
