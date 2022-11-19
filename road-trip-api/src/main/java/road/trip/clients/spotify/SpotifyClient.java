package road.trip.clients.spotify;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Data
@Component
/**
 * Uses this Spotify Java API: https://github.com/spotify-web-api-java/spotify-web-api-java
 */
public class SpotifyClient {
    @Value("${spotify.client-id}")
    private String clientId;
    private static String CLIENT_ID;
    @Value("${spotify.client-id}")
    public void setClientIdStatic(String clientId) {
        CLIENT_ID = clientId;
    }

    @Value("${spotify.client-secret}")
    private String clientSecret;
    private static String CLIENT_SECRET;
    @Value("${spotify.client-secret}")
    public void setClientSecretStatic(String clientSecret) {
        CLIENT_SECRET = clientSecret;
    }

    @Value("${spotify.redirect-uri}")
    private String redirectUri;
    private static String REDIRECT_URI;
    @Value("${spotify.redirect-uri}")
    public void setRedirectUriStatic(String redirectUri) {
        REDIRECT_URI = redirectUri;
    }

    @Value("${spotify.scope}")
    private String scope;
    private static String SCOPE;
    @Value("${spotify.scope}")
    public void setScopeStatic(String scope) {
        SCOPE = scope;
    }

    private final SpotifyApi api;

    /** Only exists to make Spring happy */
    private SpotifyClient() {
        api = new SpotifyApi.Builder().build();
    }

    /** Used by `withAccessToken` method */
    private SpotifyClient(SpotifyApi api) {
        this.api = api;
    }

    public static SpotifyClient withAccessToken(String accessToken) {
        SpotifyApi api = new SpotifyApi.Builder()
            .setAccessToken(accessToken)
            .build();
        return new SpotifyClient(api);
    }

    public static String getAuthorizationCodeURI(String state) {
        SpotifyApi api = new SpotifyApi.Builder()
            .setClientId(CLIENT_ID)
            .setRedirectUri(SpotifyHttpManager.makeUri(REDIRECT_URI))
            .build();
        AuthorizationCodeUriRequest request = api.authorizationCodeUri()
            .scope(SCOPE)
            .state(state)
            .build();
        URI uri = request.execute();
        return uri.toString();
    }

    public static AuthorizationCodeCredentials getCredentials(String code) throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi api = new SpotifyApi.Builder()
            .setClientId(CLIENT_ID)
            .setClientSecret(CLIENT_SECRET)
            .setRedirectUri(SpotifyHttpManager.makeUri(REDIRECT_URI))
            .build();
        AuthorizationCodeRequest request = api.authorizationCode(code).build();
        return request.execute();
    }

    public String refreshTokens(String refreshToken) throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi api = new SpotifyApi.Builder()
            .setClientId(CLIENT_ID)
            .setClientSecret(CLIENT_SECRET)
            .setRefreshToken(refreshToken)
            .build();
        AuthorizationCodeRefreshRequest request = api.authorizationCodeRefresh().build();
        return request.execute().getAccessToken();
    }

    public Playlist createPlaylist(String spotifyUserID, String name) throws IOException, ParseException, SpotifyWebApiException {
        CreatePlaylistRequest request = api.createPlaylist(spotifyUserID, name).build();
        return request.execute();
    }

    public List<TrackSimplified> recommendTracks(List<String> genres, Integer limit) throws IOException, ParseException, SpotifyWebApiException {
        GetRecommendationsRequest request = api.getRecommendations()
            .seed_genres(String.join(",", genres))
            .limit(limit)
            .build();
        Recommendations recommendations = request.execute();
        return List.of(recommendations.getTracks());
    }

    public Playlist getPlaylist(String id) throws IOException, ParseException, SpotifyWebApiException {
        GetPlaylistRequest request = api.getPlaylist(id).build();
        return request.execute();
    }

    public String getSpotifyUserId() throws IOException, ParseException, SpotifyWebApiException {
        GetCurrentUsersProfileRequest request = api.getCurrentUsersProfile().build();
        User user = request.execute();
        return user.getId();
    }

}
