package road.trip.api.playlist.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GeneratePlaylistRequest {
    Long tripId;
    List<String> genres;
    List<Float> energy;
    List<Float> danceability;
    List<Float> instrumentalness;
    List<Float> acousticness;
    List<Float> popularity;
    @JsonProperty("happiness")
    List<Float> valence;
    List<Integer> tempo;
    @JsonProperty("target_duration")
    Integer duration;
}
