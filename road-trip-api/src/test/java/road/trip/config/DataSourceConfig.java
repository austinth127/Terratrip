package road.trip.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Do not delete this or the database will fall apart...
 */
@TestConfiguration
public class DataSourceConfig {
    @Bean
    @Profile("test")
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1")
            .username("sa")
            .password("sa")
            .build();
    }


}
