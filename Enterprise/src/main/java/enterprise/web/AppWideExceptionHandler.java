package enterprise.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppWideExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception e, WebRequest request)
    {
        return new ResponseEntity<>(e.getMessage() + "\n" + request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
