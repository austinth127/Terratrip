package road.trip.persistence;

import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import road.trip.persistence.models.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@DisplayName("Model Tests")
public class ModelTests {

    @Nested
    @DisplayName("Genre Tests")
    class GenreTests {
    }
    @Nested
    @DisplayName("Playlist Tests")
    @Ignore
    class PlaylistTests{
        /**
         * Test to validate playlist constructor.
         */
        @Test
        @DisplayName("Playlist Constructor")
        void playlistConstructor(){
            /**
            Playlist t = new Playlist("bruh","Test","www.spotify.com");
            assertAll(
                ()->assertEquals(1l,t.getId()),
                ()->assertEquals("Test",t.getName()),
                ()->assertEquals("www.spotify.com",t.getUrl())
            );
             */
        }

    }

    @Nested
    @DisplayName("Trip Tests")
    class TripTests{

    }
    @Nested
    @DisplayName("Location Tests")
    class LocationTests {
        // Trip(Long id, String name, int adventureLevel, int duration, double distance, Date startDate, List<Location> locations)
        @Test
        @DisplayName("Location Constructor")
        void stopConstructor(){

        }

    }
    @Nested
    @DisplayName("User Tests")
    class UserTests{
        @Test
        @DisplayName("User Constructor")
        void userConstuctor() {
            /**
            User u = new User(1l, 2l, "test@gmail.com", "Bob", "bobusername", new ArrayList<>());
            assertAll(
                ()->assertEquals(1l,u.getId()),
                ()->assertEquals(2l,u.getUserfrontId()),
                ()->assertEquals("test@gmail.com",u.getEmailAddress()),
                ()->assertEquals("Bob",u.getName()),
                ()->assertEquals("bobusername",u.getUsername())
            );
             */
        }

    }


}
