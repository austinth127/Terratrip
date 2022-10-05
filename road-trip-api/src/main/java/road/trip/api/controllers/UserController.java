package road.trip.api.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import road.trip.api.requests.RegisterRequest;
import road.trip.persistence.models.User;
import road.trip.api.services.UserService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/user")
public class UserController {
    final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfile(@PathVariable(value = "id") Long userId) {
        return ResponseEntity.of(userService.getProfile(userId));
    }

}
