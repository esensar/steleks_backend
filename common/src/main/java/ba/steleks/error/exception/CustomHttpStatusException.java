package ba.steleks.error.exception;/**
 * Created by ensar on 30/05/17.
 */

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.Charset;
import java.util.logging.Logger;

public class CustomHttpStatusException extends HttpStatusCodeException {

    private String message;

    public CustomHttpStatusException(HttpStatus statusCode, String message) {
        super(statusCode);
        this.message = message;
    }

    public CustomHttpStatusException(HttpStatus statusCode, String statusText, String message) {
        super(statusCode, statusText);
        this.message = message;
    }

    public CustomHttpStatusException(HttpStatus statusCode, String statusText, byte[] responseBody, Charset responseCharset, String message) {
        super(statusCode, statusText, responseBody, responseCharset);
        this.message = message;
    }

    public CustomHttpStatusException(HttpStatus statusCode, String statusText, HttpHeaders responseHeaders, byte[] responseBody, Charset responseCharset, String message) {
        super(statusCode, statusText, responseHeaders, responseBody, responseCharset);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
