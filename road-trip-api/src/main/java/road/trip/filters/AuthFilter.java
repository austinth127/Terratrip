package road.trip.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import road.trip.config.jwt.TokenProcessor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthFilter extends GenericFilter {
    private final TokenProcessor tokenProcessor;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {

        Optional<Authentication> authentication;
        try {
            authentication = tokenProcessor.authenticate((HttpServletRequest) request);

            authentication.ifPresent(value -> SecurityContextHolder.getContext().setAuthentication(value));
        } catch (Exception e) {
            log.error("Token processing error", e);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

}
