package ba.steleks.error;

import ba.steleks.error.exception.ExternalServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 01/04/2017.
 */
@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleCustomException(javax.validation.ConstraintViolationException ex) {

        Map<String, String> map= new HashMap<String, String>();
        map.put("status", HttpStatus.BAD_REQUEST.toString());
        map.put("error", ex.getLocalizedMessage());

        return map;

    }

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public Map<String, String> handleExternalServiceException(ExternalServiceException ex) {

        Map<String, String> map= new HashMap<String, String>();
        map.put("status", HttpStatus.SERVICE_UNAVAILABLE.toString());
        map.put("error", HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase());

        return map;

    }

    @ExceptionHandler(HttpStatusCodeException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleAllExceptions(HttpStatusCodeException ex) {
        Map<String, String> map= new HashMap<String, String>();
        map.put("status", ex.getStatusCode().toString());
        map.put("error", ex.getStatusCode().getReasonPhrase());

        return ResponseEntity.status(ex.getStatusCode()).body(map);

    }

}
