package road.trip.api.playlist.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import se.michaelthelin.spotify.model_objects.specification.ExternalUrl;
import se.michaelthelin.spotify.model_objects.specification.Image;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;

@Data
@Log4j2
@NoArgsConstructor
public class PlaylistResponse {
    private String id;
    private String url;
    private String name;
    private String img;
    private Long tripId;

    public PlaylistResponse withTripId(Long tripId) {
        this.tripId = tripId;
        return this;
    }

    public PlaylistResponse(Playlist p) {
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
    }

    public PlaylistResponse(PlaylistSimplified ps) {
        id = ps.getId();
        name = ps.getName();

        // set url
        ExternalUrl urls = ps.getExternalUrls();
        if (urls != null && urls.getExternalUrls() != null) {
            url = urls.getExternalUrls().get("spotify");
        } else {
            url = null;
        }

        // set image
        Image[] images = ps.getImages();
        if (images != null && images.length > 0) {
            img = images[0].getUrl();
        } else {
            img = null;
        }
    }
}
