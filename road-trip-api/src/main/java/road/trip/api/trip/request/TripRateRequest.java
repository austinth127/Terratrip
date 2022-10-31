package road.trip.api.trip.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TripRateRequest {
    @NotNull
    private Double rating;
}
