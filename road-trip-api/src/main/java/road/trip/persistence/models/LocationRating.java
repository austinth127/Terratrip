package road.trip.persistence.models;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@Table(name = LocationRating.TABLE_NAME)
@NoArgsConstructor
@Builder
public class LocationRating {
    public static final String TABLE_NAME = "LOCATIONRATING";
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private Double rating;
    @ManyToOne(targetEntity = Location.class)
    private Location ratedLocation;
    @OneToOne(targetEntity = User.class)
    private User ratingUser;

}
