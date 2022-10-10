package road.trip.api.requests;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class TripRateRequest {
    @NotNull
    private Double rating;
}
