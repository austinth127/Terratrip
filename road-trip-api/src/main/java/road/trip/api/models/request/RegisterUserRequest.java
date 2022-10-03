package road.trip.api.models.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterUserRequest {
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private Long userId;
}
