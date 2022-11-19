package road.trip.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.api.notification.NotificationService;
import road.trip.api.user.request.RegisterRequest;
import road.trip.persistence.daos.UserRepository;
import road.trip.persistence.models.User;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationService {
    final UserService userService;
    final NotificationService notificationService;
    final UserRepository userRepository;

    public User register(RegisterRequest request) {
        User user = User.builder()
            .username(request.getUsername())
            .userfrontId(request.getUserId())
            .name(request.getName())
            .emailAddress(request.getEmail())
            .build();

        user = userRepository.save(user);
        notificationService.enqueueNotifications(user);

        return user;
    }

}
