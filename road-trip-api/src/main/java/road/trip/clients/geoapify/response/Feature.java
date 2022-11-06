package road.trip.clients.geoapify.response;

import lombok.Data;
import road.trip.persistence.models.Location;

@Data
public class Feature {
    String type;
    Properties properties;

    public Location buildLocation() {
        return Location.builder()
            .address(properties.getAddress())
            .name(properties.getName())
            .geoapifyId(properties.getPlaceId())
            .coordX(properties.getCoordX())
            .coordY(properties.getCoordY())
            .phoneContact(properties.getContact() == null ? null : properties.getContact().getPhone())
            .website(properties.getWebsite())
            .description(properties.getDescription())
            .build();
    }
}
