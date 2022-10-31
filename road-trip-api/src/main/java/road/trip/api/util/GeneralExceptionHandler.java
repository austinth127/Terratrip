package road.trip.api.util;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import road.trip.api.util.exceptions.NotFoundException;

/**
 * Handles all exceptions thrown by controllers.
 */
@ControllerAdvice
@Log4j2
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Method to handle validation exceptions (see the javax.validation API).
     *
     * @param e       exception thrown
     * @param headers ???
     * @param status  the response status (should be 400 Bad Request)
     * @param request the original request
     * @return the service's response
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error(e.getStackTrace());
        return ResponseEntity.badRequest().build();
    }

    /**
     * Method to handle Spring Security access denied exceptions.
     * Occurs whenever a user tries to access an endpoint with an incorrect user role.
     *
     * @param e exception thrown
     * @return the service's response
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<?> handle404NotFoundException(NotFoundException e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.notFound().build();
    }

    /**
     * Method to handle any exception.
     *
     * @param e exception thrown
     * @return the service's response
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<?> handleOtherExceptions(Exception e) {
        log.error(ExceptionUtils.getStackTrace(e));
        return ResponseEntity.internalServerError().build();
    }
}
