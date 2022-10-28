package road.trip.api.notification.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Setter;
import road.trip.persistence.models.NotificationType;

import java.time.LocalDateTime;

@Builder
@Setter
public class NotificationResponse {

    @JsonProperty("text")
    private String text;

    @JsonProperty("notified_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm a")
    private LocalDateTime notifiedAt;                    // 2022-10-28 10:21 pm

    @JsonProperty("type")
    private NotificationType type;

}
