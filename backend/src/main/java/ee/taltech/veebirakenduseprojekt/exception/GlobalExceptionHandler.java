package ee.taltech.veebirakenduseprojekt.exception;

import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.*;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        ErrorResponse response = new ErrorResponse();
        response.setMessage(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Internal server error", e);
        ErrorResponse response = new ErrorResponse();
        response.setMessage("Internal server error");
        return ResponseEntity.internalServerError().body(response);
    }
}
