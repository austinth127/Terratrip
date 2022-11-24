package road.trip.api.trip.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import road.trip.api.location.LocationService;
import road.trip.api.location.response.LocationResponse;
import road.trip.persistence.models.Stop;
import road.trip.persistence.models.Trip;
import road.trip.persistence.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Data
public class TripResponse {
    public TripResponse(Trip t) {
        name = t.getName();
        distance = t.getDistance();
        duration = t.getDriveDuration();
        advLevel = t.getAdventureLevel().toString();
        startDate = t.getStartDate();
        endDate = t.getEndDate();
        id = t.getId();
        start = t.getStart() == null ? null : new LocationResponse(t.getStart());
        end = t.getEnd() == null ? null : new LocationResponse(t.getEnd());
        stops = t.getStops().stream()
            .map(Stop::getLocation)
            .map(LocationResponse::new)
            .collect(Collectors.toList());
        rating = t.getRating();
        playlistId = t.getPlaylistId();
    }

    public TripResponse(Trip t, User user, LocationService locationService) {
        name = t.getName();
        distance = t.getDistance();
        duration = t.getDriveDuration();
        advLevel = t.getAdventureLevel().toString();
        startDate = t.getStartDate();
        endDate = t.getEndDate();
        id = t.getId();
        start = t.getStart() == null ? null : new LocationResponse(t.getStart(), user, locationService);
        end = t.getEnd() == null ? null : new LocationResponse(t.getEnd(), user, locationService);
        stops = t.getStops().stream()
            .map(Stop::getLocation)
            .map(l -> new LocationResponse(l, user, locationService))
            .collect(Collectors.toList());
        rating = t.getRating();
    }


    @NonNull
    private Long id;

    @NonNull
    private String name;
    @NonNull
    private String advLevel;

    private Double duration;

    private Double distance;

    private Double rating;

    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NonNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NonNull
    private LocationResponse start;
    @NonNull
    private LocationResponse end;

    private List<LocationResponse> stops;

    private String playlistId;
}
