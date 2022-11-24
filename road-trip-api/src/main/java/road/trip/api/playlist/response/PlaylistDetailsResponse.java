package road.trip.api.playlist.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Log4j2
@NoArgsConstructor
public class PlaylistDetailsResponse {
    private String id;
    private String url;
    private String name;
    private String img;
    private List<TrackResponse> tracks;

    public PlaylistDetailsResponse(Playlist p, List<IPlaylistItem> tracks) {
        id = p.getId();
        name = p.getName();

        // set url
        ExternalUrl urls = p.getExternalUrls();
        if (urls != null && urls.getExternalUrls() != null) {
            url = urls.getExternalUrls().get("spotify");
        } else {
            url = null;
        }

        // set image
        Image[] images = p.getImages();
        if (images != null && images.length > 0) {
            img = images[0].getUrl();
        } else {
            img = null;
        }

        // set track details
        this.tracks = tracks.stream().map(TrackResponse::new).collect(Collectors.toList());
    }
}
