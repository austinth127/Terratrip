package road.trip.api.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StopRequest {
    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private int rating;
    @NotNull
    private String type;
    @NotNull
    private String geoType;
    @NotNull
    private double coordX;
    @NotNull
    private double coordY;
}
