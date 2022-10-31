package road.trip.api.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.notification.response.NotificationResponse;
import road.trip.util.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications() {
        return ResponseEntity.of(Optional.of(notificationService.getNotifications()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(@PathVariable("id") Long id) throws NotFoundException {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok().build();
    }

}
