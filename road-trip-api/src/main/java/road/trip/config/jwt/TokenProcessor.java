package road.trip.config.jwt;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.List.of;


@Log4j2
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TokenProcessor {
    private final JwtConfiguration jwtConfiguration;
    private final NimbusJwtDecoder decoder;

    public Optional<Authentication> authenticate(HttpServletRequest request) throws Exception {
        String idToken = getIdToken(request);

        if (idToken != null) {
            return processIdToken(idToken);
        }
        return Optional.empty();
    }

    public Optional<Authentication> processIdToken(@NonNull String idToken) throws Exception {
        Jwt jwt = decoder.decode(idToken);
        JWTClaimsSet claims = getClaimsSet(jwt);

        validateIssuer(claims);
        String userId = getUserIdFrom(claims);

        if (userId != null) {
            User user = new User(userId, "", of());

            return Optional.of(new JwtAuthentication(user, claims, null));
        }

        return Optional.empty();
    }

    private String getIdToken(HttpServletRequest request) {
        String authHeader = request.getHeader(jwtConfiguration.getAuthHeader());
        return getBearerToken(authHeader);
    }

    private String getUserIdFrom(JWTClaimsSet claims) {
        return claims.getClaims().get(jwtConfiguration.getUserIdField()).toString();
    }

    private void validateIssuer(JWTClaimsSet claims) throws Exception {
        if (!claims.getIssuer().equals(jwtConfiguration.getIssuer())) {
            throw new Exception(String.format("Issuer '%s' does not match '%s'", claims.getIssuer(), jwtConfiguration.getIssuer()));
        }
    }

    private String getBearerToken(String token) {
        return token.startsWith("Bearer ") ? token.substring("Bearer ".length()) : token;
    }

    private JWTClaimsSet getClaimsSet(Jwt jwt) throws ParseException {
        // JWT has to have Long values instead of Instant to be parsed into a JWTClaimsSet
        Map<String, Object> json = jwt.getClaims().entrySet().stream().collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> e.getValue() instanceof Instant ? ((Instant) e.getValue()).toEpochMilli() : e.getValue()));

        return JWTClaimsSet.parse(json);
    }
}

