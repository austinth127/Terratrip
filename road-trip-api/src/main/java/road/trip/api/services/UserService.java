package road.trip.api.services;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import road.trip.persistence.models.User;
import road.trip.persistence.daos.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    final UserRepository userRepository;

    public Optional<User> findUser(Long userId) {
        return userRepository.findById(userId);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
