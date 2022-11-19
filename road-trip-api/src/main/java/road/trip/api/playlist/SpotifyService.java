package road.trip.api.playlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import road.trip.api.user.UserService;
import road.trip.clients.spotify.SpotifyClient;
import road.trip.persistence.models.User;
import road.trip.util.exceptions.BadRequestException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpotifyService {
    private final UserService userService;

    public String getAuthCodeURI() {
        return SpotifyClient.getAuthorizationCodeURI(userService.getId().toString());
    }

    /**
     * Use the given OAuth2 authorization code to request the access and
     * refresh tokens from the Spotify OAuth2 server. Then update the user's
     * Spotify info.
     *
     * @param code the OAuth2 authorization code
     */
    public void handleAuthCode(String code, String userIdString) {
        try {
            // Get the Spotify tokens
            log.debug("Getting Spotify tokens...");
            AuthorizationCodeCredentials credentials = SpotifyClient.getCredentials(code);
            String accessToken = credentials.getAccessToken();
            String refreshToken = credentials.getRefreshToken();

            // Get the Spotify user id
            log.debug("Getting Spotify user id...");
            SpotifyClient spotifyClient = SpotifyClient.withAccessToken(accessToken);
            String spotifyUserId = spotifyClient.getSpotifyUserId();

            // Parse the user id
            log.debug("Parsing Terratrip user id...");
            long userId;
            try {
                userId = Long.parseLong(userIdString);
            } catch (NumberFormatException e) {
                throw new BadRequestException(e);
            }

            // Save the Spotify information for the given user
            userService.setSpotifyInfo(userId, accessToken, refreshToken, spotifyUserId);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error(e);
        }
    }

    /**
     * Refresh all Spotify access tokens every 20 minutes to ensure
     * that access tokens don't expire (they are only valid for 60 min)
     */
    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 20)
    public void refreshTokens() {
        log.info("Beginning token refresh...");
        List<User> spotifyUsers = userService.getAllUsers().stream()
            .filter(user -> user.getSpotifyUserId() != null)
            .collect(Collectors.toList());
        log.debug("Refreshing {} token(s)", spotifyUsers.size());
        for (User user : spotifyUsers) {
            try {
                String accessToken = SpotifyClient
                    .withAccessToken(user.getSpotifyAccessToken())
                    .refreshTokens(user.getSpotifyRefreshToken());
                user.setSpotifyAccessToken(accessToken);
                userService.update(user);
            } catch (Exception e) {
                log.error("Token refresh failure", e);
            }
        }
        log.info("Token refresh complete");
    }
}
