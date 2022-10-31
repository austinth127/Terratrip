package road.trip.api.playlist;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.persistence.models.Genre;
import road.trip.persistence.models.Playlist;
import road.trip.persistence.models.Trip;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlaylistService {

    @Deprecated
    Playlist getPlaylist(long playlistID){
        return new Playlist();
    }

    @Deprecated
    void savePlaylist(String name, String url, Genre genre, long userID, long tripID){
        //TODO: write actual code
    }

    @Deprecated
    Playlist generatePlaylist(Trip t, Genre g){
        return new Playlist();
    }
}
