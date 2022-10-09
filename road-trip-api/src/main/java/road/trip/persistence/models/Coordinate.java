package road.trip.persistence.models;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinate implements Serializable {
    double longitude;
    double latitude;
}
