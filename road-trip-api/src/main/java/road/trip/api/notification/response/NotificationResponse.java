package road.trip.api.notification.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import road.trip.persistence.models.Notification;
import road.trip.persistence.models.NotificationType;

import java.time.LocalDateTime;

@Setter
@Getter
public class NotificationResponse {

    public NotificationResponse(Notification n) {
        id = n.getId();
        if(n.getTrip() != null){
            tripId = n.getTrip().getId();
        }
        notifiedAt = n.getSendAt();
        type = n.getType();

        switch(n.getType()) {
            case NEW_ACCOUNT -> {
                title = "Welcome to Terratrip!";
                body = "You're all set. Time to plan some trips!";
            }
            case UPCOMING_TRIP_WEEK -> {
                title = "Upcoming Trip!";
                body = "Your trip \"" + n.getTrip().getName() + "\" is coming up in one week!";
            }
            case UPCOMING_TRIP_DAY -> {
                title = "Upcoming Trip!";
                body = "Your trip \"" + n.getTrip().getName() + "\" is coming up tomorrow!";
            }
            case COMPLETED_TRIP -> {
                title = "Completed Trip!";
                body = "Make sure to rate your trip: \"" + n.getTrip().getName() + "\"!";
            }
            default -> throw new IllegalStateException("Oops, not implemented");
        }

    }

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("body")
    private String body;

    @JsonProperty("trip_id")
    private Long tripId;

    @JsonProperty("notified_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime notifiedAt;                    // 2022-10-28 10:21 pm

    @JsonProperty("type")
    private NotificationType type;

}
