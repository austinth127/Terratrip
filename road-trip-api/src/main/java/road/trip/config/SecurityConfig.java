package road.trip.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
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
        "/ping",
        "/spotify/auth-callback"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Handle Cross-Origin Resource Sharing
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedMethods(List.of("POST", "GET", "PUT", "PATCH", "DELETE"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowedOrigins(List.of("http://localhost", "http://127.0.0.1", "http://localhost:3000", "http://127.0.0.1:3000", "https://terratrip.gq"));
        http.cors().configurationSource(request -> corsConfig);

        // Protect against Cross-Site Request Forgery attacks
        // Expects X-XSRF-Token header
        http.csrf()
            .disable();
//            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            .ignoringAntMatchers(PUBLIC_ROUTES);

        // Set up user authentication
        http.authorizeRequests()
            .antMatchers(PUBLIC_ROUTES).permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
