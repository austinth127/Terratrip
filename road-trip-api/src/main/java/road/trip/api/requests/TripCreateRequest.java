package road.trip.api.requests;

import lombok.Data;
import road.trip.persistence.models.Stop;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class TripCreateRequest {

    @NotNull
    private String name;
    @NotNull
    private int adventureLevel;
    @NotNull
    private int duration;
    @NotNull
    private double distance;
    @NotNull
    private Date startDate;
//
//    @NotNull
//    private Stop start;
//    @NotNull
//    private Stop end;

}
