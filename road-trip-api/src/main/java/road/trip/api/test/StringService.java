package road.trip.api.test;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import road.trip.api.endpoint.response.ReverseResponse;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StringService {

    public ReverseResponse reverseString(String value) {
        return new ReverseResponse(StringUtils.reverse(value));
    }



}
