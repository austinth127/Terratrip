package road.trip.clients.spotify;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Range<T> {
    private T min;
    private T target;
    private T max;

    public Range(T min, T max) {
        this.min = min;
        this.max = max;
        this.target = null;
    }

    public Range() {
        min = null;
        target = null;
        max = null;
    }
}
