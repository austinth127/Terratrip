package road.trip.api.playlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spotify")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class SpotifyController {

    private final SpotifyService spotifyService;

    @GetMapping("/auth-uri")
    public ResponseEntity<String> getAuthURI() {
        return ResponseEntity.ok(spotifyService.getAuthCodeURI());
    }

    @GetMapping("/auth-callback")
    public ResponseEntity<?> authCallback(@RequestParam(required = false) String code, @RequestParam String state, @RequestParam(required = false) String error) {
        if (error == null) {
            spotifyService.handleAuthCode(code, state);
        } else {
            log.error("Unsuccessful Spotify OAuth Flow: {}", error);
        }
        return ResponseEntity.ok().build();
    }

}
