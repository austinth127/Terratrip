package road.trip.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
@Setter
public class JwtConfiguration {
  @Value("${tenant-id}")
  private String tenantId;
  private String jwkUrl;

  public String getJwkUrl() {
    return StringUtils.isNotEmpty(jwkUrl) ? jwkUrl : String.format("https://api.userfront.com/v0/tenants/%s/jwks", tenantId);
  }
}
