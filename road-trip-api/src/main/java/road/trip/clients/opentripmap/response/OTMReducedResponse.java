package road.trip.clients.opentripmap.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OTMReducedResponse {
    @JsonProperty("xid")
    String otmId;
    String name;
    String osm;
    OTMPoint point;
    @JsonProperty("kinds")
    String categories;
}
