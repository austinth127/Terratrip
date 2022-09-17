package road.trip.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import road.trip.api.endpoint.UserEndpoint;
import road.trip.api.user.UserService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class RoadTripApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoadTripApplication.class, args);
    }
}
