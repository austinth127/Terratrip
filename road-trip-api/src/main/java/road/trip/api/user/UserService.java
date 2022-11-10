package road.trip.api.user;

import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import road.trip.api.notification.NotificationService;
import road.trip.api.user.request.RegisterRequest;
import road.trip.config.jwt.JwtAuthentication;
import road.trip.persistence.daos.NotificationRepository;
import road.trip.persistence.models.User;
import road.trip.persistence.daos.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    final UserRepository userRepository;


    /**
     * Extracts a Spring Security User object from the Spring Security Context
     * (the Spring Security Context gets set by our custom AuthFilter class).
     */
    private org.springframework.security.core.userdetails.User springSecurityUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication();
        try {
            return ((org.springframework.security.core.userdetails.User) ((JwtAuthentication) principal).getPrincipal());
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Gets the current user.
     *
     * @return The user that made the request.
     */
    public User user() {
        if (springSecurityUser() == null) {
            return null;
        }
        String username = Objects.requireNonNull(springSecurityUser()).getUsername();
        return userRepository.findByUserfrontId(Long.parseLong(username)).orElse(null);
    }

    /**
     * Gets the id of the current user.
     *
     * @return The id of the user that made the request.
     */
    public Long getId() {
        if (user() == null) {
            return null;
        }
        return user().getId();
    }

    public void setSpotifyInfo(String accessToken, String refreshToken, String spotifyUserID) {
        User user = user();
        user.setSpotifyAccessToken(accessToken);
        user.setSpotifyRefreshToken(refreshToken);
        user.setSpotifyUserId(spotifyUserID);
        userRepository.save(user);
    }
}
