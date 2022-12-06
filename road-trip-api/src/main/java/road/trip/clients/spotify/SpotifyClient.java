package road.trip.clients.spotify;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.IPlaylistItem;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import se.michaelthelin.spotify.requests.data.follow.UnfollowPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.*;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
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

    public void addTracksToPlaylist(String playlistId, List<String> trackUris) throws IOException, ParseException, SpotifyWebApiException {
        AddItemsToPlaylistRequest request = api.addItemsToPlaylist(playlistId, trackUris.toArray(String[]::new)).build();
        request.execute();
    }

    /**
     * Clears the given playlist of all items.
     *
     * @param playlistId the id of the playlist to clear
     */
    public void clearPlaylist(String playlistId) throws IOException, ParseException, SpotifyWebApiException {
        final int limit = 50;

        GetPlaylistsItemsRequest itemsRequest = api.getPlaylistsItems(playlistId)
            .additionalTypes("track")
            .limit(limit)
            .build();
        Paging<PlaylistTrack> page;
        JsonArray tracks;
        RemoveItemsFromPlaylistRequest removeItemsRequest;

        do {
            page = itemsRequest.execute();
            tracks = new JsonArray();
            List<JsonObject> trackUris = Arrays.stream(page.getItems()).toList().stream()
                .map(PlaylistTrack::getTrack)
                .map(IPlaylistItem::getUri)
                .map(uri -> {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("uri", uri);
                    return jo;
                })
                .collect(Collectors.toList());
            for (JsonObject uri : trackUris) {
                tracks.add(uri);
            }
            removeItemsRequest = api.removeItemsFromPlaylist(playlistId, tracks).build();
            removeItemsRequest.execute();
        } while (page.getNext() != null);
    }

    public void deletePlaylist(String playlistId) throws IOException, ParseException, SpotifyWebApiException {
        UnfollowPlaylistRequest request = api.unfollowPlaylist(playlistId).build();
        request.execute();
    }

    public List<IPlaylistItem> getPlaylistItems(String playlistId) throws IOException, ParseException, SpotifyWebApiException {
        final int limit = 50;
        int offset = 0;

        GetPlaylistsItemsRequest.Builder builder = api.getPlaylistsItems(playlistId)
            .additionalTypes("track")
            .limit(limit);
        Paging<PlaylistTrack> page;
        List<IPlaylistItem> items = new ArrayList<>();

        // Get all results
        do {
            builder.offset(offset);
            offset += limit;
            page = builder.build().execute();
            items.addAll(Arrays.stream(page.getItems())
                .map(PlaylistTrack::getTrack)
                .collect(Collectors.toList()));
        } while (page.getNext() != null);

        return items;
    }

    public List<String> getAvailableGenres() throws IOException, ParseException, SpotifyWebApiException {
        GetAvailableGenreSeedsRequest request = api.getAvailableGenreSeeds().build();
        return List.of(request.execute());
    }

    /**
     * Recommends a list of tracks from the given parameters
     *
     * @param genres Should all be available genres in the Spotify API
     * @param audioFeatures Defines the min/max/target for various audio features (energy,
     *                      danceability, instrumentalness, acousticness, valence, and tempo)
     *                      Valence is basically a fancy word for "happiness".
     *                      Each audio feature min/max/target has a value between 0 and 1, except
     *                      tempo (measured in BPM).
     * @param popularity Defines the min/max/target popularity. 0 <= popularity <= 100
     * @param limit Defines the number of tracks to get. 1 <= limit <= 100
     *
     * @return The list of recommended tracks
     *
     * @throws IOException
     * @throws ParseException
     * @throws SpotifyWebApiException
     */
    public List<TrackSimplified> recommendTracks(List<String> genres,
                                                 AudioFeatures audioFeatures,
                                                 Range<Integer> popularity,
                                                 Integer limit) throws IOException, ParseException, SpotifyWebApiException {
        if (limit < 1 || limit > 100) {
            throw new SpotifyWebApiException("Limit out of range");
        }
        AudioFeatures a = audioFeatures;
        Range<Float> energy = a.getEnergy();
        Range<Float> danceability = a.getDanceability();
        Range<Float> instrumentalness = a.getInstrumentalness();
        Range<Float> acousticness = a.getAcousticness();
        Range<Float> valence = a.getValence();
        Range<Float> tempo = a.getTempo();

        log.debug("Max energy: " + energy.getMax());
        log.debug("Min energy: " + energy.getMin());

        GetRecommendationsRequest request = api.getRecommendations()
            .seed_genres(String.join(",", genres))
            .min_energy(energy.getMin())
            .max_energy(energy.getMax())
            .min_danceability(danceability.getMin())
            .max_danceability(danceability.getMax())
            .min_instrumentalness(instrumentalness.getMin())
            .max_instrumentalness(instrumentalness.getMax())
            .min_acousticness(acousticness.getMin())
            .max_acousticness(acousticness.getMax())
            .min_valence(valence.getMin())
            .max_valence(valence.getMax())
            .min_tempo(tempo.getMin())
            .max_tempo(tempo.getMax())
            .min_popularity(popularity.getMin())
            .max_popularity(popularity.getMax())
            .limit(limit)
            .build();
        Recommendations recommendations = request.execute();
        return List.of(recommendations.getTracks());
    }

    public Playlist getPlaylist(String id) throws IOException, ParseException, SpotifyWebApiException {
        GetPlaylistRequest request = api.getPlaylist(id).build();
        return request.execute();
    }

    public List<PlaylistSimplified> getMyPlaylists() throws IOException, ParseException, SpotifyWebApiException {
        GetListOfCurrentUsersPlaylistsRequest request = api.getListOfCurrentUsersPlaylists()
            .limit(50) // max value is 50, assume no more than 50 playlists
            .build();
        return Arrays.stream(request.execute().getItems()).toList();
    }

    public String getSpotifyUserId() throws IOException, ParseException, SpotifyWebApiException {
        GetCurrentUsersProfileRequest request = api.getCurrentUsersProfile().build();
        User user = request.execute();
        return user.getId();
    }

}
