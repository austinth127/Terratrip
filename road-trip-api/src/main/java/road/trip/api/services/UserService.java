package road.trip.api.services;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import road.trip.api.requests.RegisterRequest;
import road.trip.config.jwt.JwtAuthentication;
import road.trip.persistence.models.User;
import road.trip.persistence.daos.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    final UserRepository userRepository;

    public Optional<User> getProfile(Long userId) {
        return userRepository.findByUserfrontId(userId);
    }

    public User register(RegisterRequest request) {
        User user = User.builder()
            .username(request.getUsername())
            .userfrontId(request.getUserId())
            .name(request.getName())
            .emailAddress(request.getEmail())
            .build();
        return userRepository.save(user);
    }

    public org.springframework.security.core.userdetails.User springSecurityUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication();
        try {
            return ((org.springframework.security.core.userdetails.User) ((JwtAuthentication) principal).getPrincipal());
        } catch (ClassCastException e) {
            return null;
        }
    }

    public User user() {
        if (springSecurityUser() == null) {
            return null;
        }
        return userRepository.findByUserfrontId(Long.parseLong(user().getUsername())).orElse(null);
    }

    public Long getId() {
        if (user() == null) {
            return null;
        }
        return user().getId();
    }
}
