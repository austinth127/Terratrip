package road.trip.api.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import road.trip.api.endpoint.response.ReverseResponse;
import road.trip.api.test.StringService;

@Log4j2
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PingEndpoint {

    private final StringService stringService;


    @GetMapping("/ping")
    public String ping() {
        return "pong!";
    }

    @GetMapping("/memory-ping")
    public String memoryPing() {
        return String.format("Max available memory: %.3f MB", (Runtime.getRuntime().maxMemory() / (1024.0 * 1024.0)));
    }

    @GetMapping("/string-reverse")
    @CrossOrigin
    public ResponseEntity<ReverseResponse> reverseString(String value) {
        return ResponseEntity.ok(stringService.reverseString(value));
    }
}