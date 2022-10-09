package road.trip.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import road.trip.filters.AuthFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig {
    private final AuthFilter authFilter;

    private static final String[] PUBLIC_ROUTES = {
        "/user/register",
        "/user/profile/{id}",
        "/create-stop",
        "/create-trip"
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
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
            .cors().configurationSource(request -> corsConfig)
            .and()

            // TODO: Unsafe, fix for milestone 3
            .csrf().disable();

        return http.build();
    }

}
