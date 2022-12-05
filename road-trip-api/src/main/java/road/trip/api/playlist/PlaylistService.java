package road.trip.api.playlist;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.playlist.request.GeneratePlaylistRequest;
import road.trip.api.playlist.response.PlaylistDetailsResponse;
import road.trip.api.playlist.response.PlaylistResponse;
import road.trip.api.user.UserService;
import road.trip.persistence.daos.TripRepository;
import road.trip.persistence.models.Trip;
import road.trip.util.exceptions.NotFoundException;
import road.trip.util.exceptions.UnauthorizedException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlaylistService {

    final TripRepository tripRepository;
    final SpotifyService spotifyService;
    final UserService userService;

    /**
     * Gets all playlists in the user's music library
     */
    public List<PlaylistResponse> getMyPlaylists() {
        return spotifyService.getMyPlaylists();
    }

    /**
     * Gets all playlists that are associated with a trip owned by the user
     */
    public List<PlaylistResponse> getMyActivePlaylists() {
        List<Trip> trips = tripRepository.findByCreator_Id(userService.getId());
        return trips.stream()
            .map(Trip::getPlaylistId)
            .filter(Objects::nonNull)
            .map(spotifyService::getPlaylist)
            .collect(Collectors.toList());
    }

    /**
     * Gets all genres the user can pick from, sorted by relevance
     */
    public List<String> getAvailableGenres() {
        return spotifyService.getAvailableGenres();
    }

    public PlaylistResponse generatePlaylist(GeneratePlaylistRequest request) {
        return spotifyService.generatePlaylist(request);
    }

    public PlaylistDetailsResponse getPlaylistDetails(String id) {
        return spotifyService.getPlaylistDetails(id);
    }

    public void deletePlaylistIfMineAndGenerated(String playlistId) {
        spotifyService.deletePlaylistIfMineAndGenerated(playlistId);
    }

    /**
     * Gets the playlist for the given trip
     */
    public PlaylistResponse getTripPlaylist(Long tripId) {
        Optional<Trip> optTrip = tripRepository.findById(tripId);
        if (optTrip.isEmpty()) {
            throw new NotFoundException("Trip " + tripId + " not found");
        }
        Trip trip = optTrip.get();
        if (trip.getCreator() != userService.user()) {
            throw new UnauthorizedException();
        }

        String playlistId = trip.getPlaylistId();

        // Trip doesn't have a playlist yet
        if (playlistId == null) {
            return new PlaylistResponse();
        }

        // Get the PlaylistResponse object for the given playlist id
        return spotifyService.getPlaylist(playlistId);
    }
}
