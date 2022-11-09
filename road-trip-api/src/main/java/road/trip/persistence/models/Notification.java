package road.trip.persistence.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Trip.class)
    private Trip trip;

    @ManyToOne(targetEntity = User.class)
    private User user;

    private NotificationType type;

    private LocalDateTime sendAt;
    private LocalDateTime expireAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Notification notification = (Notification) o;
        return id != null && Objects.equals(id, notification.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
