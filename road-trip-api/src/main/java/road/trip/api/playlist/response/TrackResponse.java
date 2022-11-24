package road.trip.api.playlist.response;

import lombok.Data;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TrackResponse {
    private String url;
    private String name;
    private String album;
    private List<String> artists;
    private String img;

    public TrackResponse(IPlaylistItem i) {
        // set name
        name = i.getName();

        // set url
        ExternalUrl urls = i.getExternalUrls();
        if (urls != null && urls.getExternalUrls() != null) {
            url = urls.getExternalUrls().get("spotify");
        } else {
            url = null;
        }

        // set track/episode specific info
        Image[] images;
        switch (i.getType()) {
            case TRACK -> {
                Track t = (Track) i;
                album = t.getAlbum().getName();
                artists = Arrays.stream(t.getArtists())
                    .map(ArtistSimplified::getName)
                    .collect(Collectors.toList());

                // set image
                images = t.getAlbum().getImages();
                if (images != null && images.length > 0) {
                    img = images[0].getUrl();
                } else {
                    img = null;
                }
            }
            case EPISODE -> {
                Episode e = (Episode) i;
                album = null;
                artists = List.of(e.getShow() != null ? e.getShow().getName() : "");

                // set image
                images = e.getImages();
                if (images != null && images.length > 0) {
                    img = images[0].getUrl();
                } else {
                    img = null;
                }
            }
            default -> throw new IllegalStateException("Item must either be a track or episode");
        }

    }
}
