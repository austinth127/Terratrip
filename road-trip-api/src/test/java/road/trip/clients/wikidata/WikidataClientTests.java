package road.trip.clients.wikidata;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Log4j2
public class WikidataClientTests {
    @Autowired
    WikidataClient client;

    @Test
    void testGetImageUrlFromWikidataId() {
        String imageUrl = client.getImageUrlFromWikidataId("Q160409");
        assertNotNull(imageUrl);
        log.debug(imageUrl);
    }

}
