package road.trip.util.exceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super();
    }
    public ForbiddenException(String message) {
        super(message);
    }
    public ForbiddenException(Throwable cause) {
        super(cause);
    }
}
