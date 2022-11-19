package road.trip.clients.geoapify.response;

import lombok.Data;
import road.trip.persistence.models.Location;

import java.util.List;

@Data
public class Feature {
    String type;
    Properties properties;
    List<String> categories;

    public Location buildLocation() {
        return Location.builder()
            .address(properties.getAddress())
            .name(properties.getName())
            .geoapifyId(properties.getPlaceId())
            .coordX(properties.getCoordX())
            .coordY(properties.getCoordY())
            .categories(String.join(",", properties.getCategories()))
            .phoneContact(properties.getContact() == null ? null : properties.getContact().getPhone())
            .website(properties.getWebsite())
            .description(properties.getDescription())
            .rating(0.0)
                .mapboxId(null)
                .geoapifyId(null)
                .otmId(null)
            .build();
    }
}
