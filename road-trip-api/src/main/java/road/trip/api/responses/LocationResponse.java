package road.trip.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class LocationResponse {
    @NotNull
    private Long id;

    @NotNull @JsonProperty("place_name")
    private String name;

    @NotNull
    private Double[] center;

    private String description;
    private int rating;

}
