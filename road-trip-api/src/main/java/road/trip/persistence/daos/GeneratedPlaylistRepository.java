package road.trip.persistence.daos;

import org.springframework.data.jpa.repository.JpaRepository;
import road.trip.persistence.models.GeneratedPlaylist;

public interface GeneratedPlaylistRepository extends JpaRepository<GeneratedPlaylist, String> {
}
