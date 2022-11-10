package road.trip.api.playlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.user.UserService;
import road.trip.clients.spotify.SpotifyClient;
import road.trip.persistence.daos.UserRepository;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpotifyService {

    UserService userService;

    public void handleAuthCode(String code) {
        try {
            AuthorizationCodeCredentials credentials = SpotifyClient.getCredentials(code);
            String accessToken = credentials.getAccessToken();
            String refreshToken = credentials.getRefreshToken();
            SpotifyClient spotifyClient = SpotifyClient.withAccessToken(accessToken);
            String spotifyUserId = spotifyClient.getSpotifyUserId();

            userService.setSpotifyInfo(accessToken, refreshToken, spotifyUserId);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
