package ba.steleks.controller;

import ba.steleks.controller.storage.MediaStorageHandler;
import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Media;
import ba.steleks.repository.MediaJpaRepository;
import ba.steleks.storage.StorageService;
import ba.steleks.util.ProxyHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by admin on 16/04/2017.
 */
@RestController
public class EventGalleryController {

    private final StorageService storageService;
    private final MediaJpaRepository repository;
    private final MediaStorageHandler mediaStorageHandler;

    @Autowired
    public EventGalleryController(StorageService storageService, MediaJpaRepository repository, MediaStorageHandler mediaStorageHandler) {
        this.storageService = storageService;
        this.repository = repository;
        this.mediaStorageHandler = mediaStorageHandler;
    }

    @GetMapping("/eventPictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource("eventPictures/"+filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping("/medias/file")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestHeader(ProxyHeaders.USER_ID) String userId,
                                   RedirectAttributes redirectAttributes) throws ExternalServiceException {

        try {
            Media media = new Media();
            media.setCreatedById(Long.parseLong(userId));
            Media result = repository.save(media);

            String url = mediaStorageHandler.saveMedia(file, result.getId());
            result.setContentUrl(url);
            repository.save(result);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/medias/{id}")
                    .buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).body(result);
        } catch (Exception ex) {
            if (ex instanceof HttpClientErrorException && ((HttpClientErrorException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            } else {
                throw new ExternalServiceException();
            }
        }
    }
}
