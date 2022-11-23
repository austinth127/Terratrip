package road.trip.clients.spotify;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AudioFeatures {
    Range<Float> energy;
    Range<Float> danceability;
    Range<Float> instrumentalness;
    Range<Float> acousticness;
    Range<Float> valence;
    Range<Float> tempo;
}
