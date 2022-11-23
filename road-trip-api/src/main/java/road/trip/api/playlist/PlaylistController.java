package road.trip.api.playlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.playlist.request.GeneratePlaylistRequest;
import road.trip.api.playlist.response.PlaylistDetailsResponse;
import road.trip.api.playlist.response.PlaylistResponse;

import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class PlaylistController {
    private final PlaylistService playlistService;

    private static final Integer MAX_DURATION = 300; // 300 minutes (5 hours)

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> getMyPlaylists(Long tripId) {
        if (tripId == null) {
            return ResponseEntity.ok(playlistService.getMyPlaylists());
        } else {
            return ResponseEntity.ok(List.of(playlistService.getTripPlaylist(tripId)));
        }
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getAvailableGenres() {
        return ResponseEntity.ok(playlistService.getAvailableGenres());
    }

    @PostMapping("/generate")
    public ResponseEntity<PlaylistResponse> generatePlaylist(@RequestBody GeneratePlaylistRequest request) {
        if (request.getDuration() > MAX_DURATION) {
            log.error("Bro this playlist is WAY too long");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(playlistService.generatePlaylist(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDetailsResponse> getPlaylistDetails(@PathVariable("id") String id) {
        return ResponseEntity.ok(playlistService.getPlaylistDetails(id));
    }
}
