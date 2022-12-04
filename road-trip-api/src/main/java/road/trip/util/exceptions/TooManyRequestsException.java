package road.trip.util.exceptions;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException() {
        super();
    }
    public TooManyRequestsException(String message) {
        super(message);
    }
    public TooManyRequestsException(Throwable cause) {
        super(cause);
    }
}
