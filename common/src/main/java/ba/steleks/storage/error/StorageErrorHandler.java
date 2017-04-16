package ba.steleks.storage.error;/**
 * Created by ensar on 16/04/17.
 */

import ba.steleks.storage.error.exception.StorageFileNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.logging.Logger;

@ControllerAdvice
public class StorageErrorHandler {
    private static final Logger logger =
            Logger.getLogger(StorageErrorHandler.class.getName());

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
