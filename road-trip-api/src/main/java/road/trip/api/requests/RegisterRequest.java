package road.trip.api.requests;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterRequest {
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    private Long userId;
}
