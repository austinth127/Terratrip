package road.trip.persistence.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter @Setter @ToString @AllArgsConstructor
@NoArgsConstructor
@Table(name = "generated_playlist")
@Builder
public class GeneratedPlaylist {
    @Id
    private String playlistId; // comes from spotify

    @ManyToOne
    User user;
}
