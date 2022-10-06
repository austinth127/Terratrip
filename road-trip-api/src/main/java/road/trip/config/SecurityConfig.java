package road.trip.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final String[] PUBLIC_ROUTES = {
        "/user/register",
        "/user/profile/{id}"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedMethods(List.of("POST", "GET", "PUT", "PATCH", "DELETE"));
        corsConfig.setAllowedHeaders(List.of("*"));

        // TODO: This is unsafe, change to allowing origins of localhost and the server
        corsConfig.setAllowedOrigins(List.of("*"));

        http.authorizeRequests()
            .antMatchers(PUBLIC_ROUTES).permitAll()
            .anyRequest().authenticated()
            .and()
            //.addFilterBefore(authFilter, AuthFilter.class)
            .cors().configurationSource(request -> corsConfig)
            .and()

            // TODO: Unsafe, fix for milestone 3
            .csrf().disable();

        return http.build();
    }

}
