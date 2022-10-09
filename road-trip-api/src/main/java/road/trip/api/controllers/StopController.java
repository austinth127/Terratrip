package road.trip.api.controllers;


import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import road.trip.api.requests.StopRequest;
import road.trip.api.services.StopService;
import road.trip.api.services.TripService;
import road.trip.persistence.models.Stop;

@RestController
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StopController {

    final StopService stopService;

    @PostMapping("/create-stop")
    public ResponseEntity<Stop> saveStop(@RequestBody StopRequest stopRequest) {
        return ResponseEntity.ok(stopService.createStop(stopRequest));
    }

}
