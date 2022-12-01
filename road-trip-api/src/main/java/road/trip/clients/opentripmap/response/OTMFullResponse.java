package road.trip.clients.opentripmap.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OTMFullResponse {
    @JsonProperty("xid")
    String otmId;
    String name;
    Address address;
    String rate;
    @JsonProperty("kinds")
    String categories;
    OTMPoint point;
    @JsonProperty("preview")
    Image image;
    @JsonProperty("url")
    String website;
    @JsonProperty("wikipedia_extracts")
    Description description;

    @Data
    public static class Description {
        String text;
    }

    @Data
    public static class Image {
        String url;
        Integer height;
        Integer width;
    }

    @Data
    public static class Address {
        String city;
        String road;
        String county;
        String suburb;
        String country;
        Integer postcode;
        @JsonProperty("country_code")
        String countryCode;
        @JsonProperty("house_number")
        Integer houseNumber;
        String neighbourhood;
    }
}
