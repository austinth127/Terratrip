package road.trip.api.playlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import road.trip.api.playlist.request.GeneratePlaylistRequest;
import road.trip.api.playlist.response.PlaylistDetailsResponse;
import road.trip.api.playlist.response.PlaylistResponse;
import road.trip.api.user.UserService;
import road.trip.clients.spotify.AudioFeatures;
import road.trip.clients.spotify.Range;
import road.trip.clients.spotify.SpotifyClient;
import road.trip.persistence.daos.GeneratedPlaylistRepository;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.GeneratedPlaylist;
import road.trip.persistence.models.Trip;
import road.trip.persistence.models.User;
import road.trip.util.exceptions.BadRequestException;
import road.trip.util.exceptions.NotFoundException;
import road.trip.util.exceptions.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpotifyService {
    private final UserService userService;
    private final TripRepository tripRepository;
    private final GeneratedPlaylistRepository gpRepository;

    private static final float AVG_SONG_DURATION = 3.283f; // 3 minutes, 17 sec

    public String getAuthCodeURI() {
        return SpotifyClient.getAuthorizationCodeURI(userService.getId().toString());
    }

    private SpotifyClient client() {
        return SpotifyClient.withAccessToken(userService.user().getSpotifyAccessToken());
    }

    private String getSpotifyUserId() {
        return userService.user().getSpotifyUserId();
    }

    public List<PlaylistResponse> getMyPlaylists() {
        try {
            return client().getMyPlaylists().stream()
                .map(PlaylistResponse::new)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAvailableGenres() {
        String[] popularGenres = {"road-trip", "pop", "rock", "edm", "electro",
            "electronic", "alternative", "lounge", "house", "indie-pop", "electronic", "techno",
        "r-n-b", "metal", "hard-rock", "hip-hop", "dance", "classical"};
        try {
            return client().getAvailableGenres().stream().sorted((o1, o2) -> {
                if (!o1.equals(o2)) {
                    if (Arrays.asList(popularGenres).contains(o1)) {
                        return -1;
                    }
                    if (Arrays.asList(popularGenres).contains(o2)) {
                        return 1;
                    }
                }
                return o1.compareTo(o2);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PlaylistResponse getPlaylist(String id) {
        try {
            return new PlaylistResponse(client().getPlaylist(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PlaylistDetailsResponse getPlaylistDetails(String id) {
        SpotifyClient client = client();
        try {
            return new PlaylistDetailsResponse(client.getPlaylist(id), client.getPlaylistItems(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deletePlaylistIfMineAndGenerated(String playlistId) {
        Optional<GeneratedPlaylist> playlist = playlistId == null ? Optional.empty() : gpRepository.findById(playlistId);

        if (playlist.isPresent() && playlist.get().getUser() == userService.user()) {
            try {
                client().deletePlaylist(playlistId);
                log.info("Deleted playlist " + playlistId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public PlaylistResponse generatePlaylist(GeneratePlaylistRequest request) {
        SpotifyClient client = client();
        Optional<Trip> optTrip = tripRepository.findById(request.getTripId());
        if (optTrip.isEmpty()) {
            throw new NotFoundException("trip " + request.getTripId() + " not found");
        }
        Trip trip = optTrip.get();
        if (trip.getCreator() != userService.user()) {
            throw new UnauthorizedException();
        }

        // Make sure we have a clean playlist to work with
        String playlistId = trip.getPlaylistId();
        deletePlaylistIfMineAndGenerated(playlistId);

        // create a new playlist
        String playlistName = trip.getName() + " Playlist";
        try {
            playlistId = client.createPlaylist(getSpotifyUserId(), playlistName).getId();
            trip.setPlaylistId(playlistId);
            tripRepository.save(trip);

            // Label the playlist as "generated"
            GeneratedPlaylist gp = GeneratedPlaylist.builder()
                .playlistId(playlistId)
                .user(userService.user())
                .build();
            gpRepository.save(gp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Actually generate the songs
        List<Float> energy = request.getEnergy();
        List<Float> danceability = request.getDanceability();
        List<Float> instrumentalness = request.getInstrumentalness();
        List<Float> acousticness = request.getAcousticness();
        List<Float> valence = request.getValence();
        List<Integer> tempo = request.getTempo();
        AudioFeatures af = AudioFeatures.builder()
            .energy(energy == null ? new Range<>(0f, 1f) : new Range<>(energy.get(0), energy.get(1)))
            .danceability(danceability == null ? new Range<>(0f, 1f) : new Range<>(danceability.get(0), danceability.get(1)))
            .instrumentalness(instrumentalness == null ? new Range<>(0f, 1f) : new Range<>(instrumentalness.get(0), instrumentalness.get(1)))
            .acousticness(acousticness == null ? new Range<>(0f, 1f) : new Range<>(acousticness.get(0), acousticness.get(1)))
            .valence(valence == null ? new Range<>(0f, 1f) : new Range<>(valence.get(0), valence.get(1)))
            .tempo(tempo == null ? new Range<>(0f, 1000f) : new Range<>(tempo.get(0).floatValue(), tempo.get(1).floatValue()))
            .build();

        int numSongs = Math.round(request.getDuration() / AVG_SONG_DURATION);

        List<Integer> popularity = request.getPopularity().stream().map(f -> Math.round(f * 100)).collect(Collectors.toList());
        if (popularity.isEmpty()) {
            popularity = List.of(0, 100);
        }

        try {
            // @TODO this can throw index out of bounds exception
            List<TrackSimplified> tracks = client.recommendTracks(request.getGenres(), af, new Range<>(popularity.get(0), popularity.get(1)), numSongs);
            List<String> trackUris = tracks.stream().map(TrackSimplified::getUri).collect(Collectors.toList());
            client.addTracksToPlaylist(playlistId, trackUris);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return getPlaylist(playlistId);
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
