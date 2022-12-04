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
    String osm;
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
        @Override
        public String toString() {
            return text;
        }
    }

    @Data
    public static class Image {
        @JsonProperty("source")
        String url;
        Integer height;
        Integer width;
        @Override
        public String toString() {
            return url;
        }
    }

    @Data
    public static class Address {
        String city;
        String road;
        String county;
        String suburb;
        String country;
        String postcode;
        @JsonProperty("country_code")
        String countryCode;
        @JsonProperty("house_number")
        String houseNumber;
        String neighbourhood;

        @Override
        public String toString() {
            return
                (road == null ? "" : road + " ") +
                (city + ", " + countryCode.toUpperCase()) +
                (postcode == null ? "" : " " + postcode);
        }
    }
}
