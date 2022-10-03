package road.trip.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtDecoderFactory {
  private final JwtConfiguration jwtConfiguration;

  @Bean
  public NimbusJwtDecoder createDecoder() {
    return NimbusJwtDecoder.withJwkSetUri(jwtConfiguration.getJwkUrl()).build();
  }
}
