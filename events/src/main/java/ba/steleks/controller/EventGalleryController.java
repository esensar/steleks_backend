package ba.steleks.controller;

import ba.steleks.controller.storage.MediaStorageHandler;
import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Media;
import ba.steleks.repository.MediaJpaRepository;

import ba.steleks.service.discovery.ServiceDiscoveryClient;
import ba.steleks.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by admin on 16/04/2017.
 */
@RestController
public class EventGalleryController {

    private final StorageService storageService;
    private final MediaJpaRepository repository;
    private final ServiceDiscoveryClient discoveryClient;
    private final MediaStorageHandler mediaStorageHandler;

    @Autowired
    public EventGalleryController(StorageService storageService, MediaJpaRepository repository, ServiceDiscoveryClient discoveryClient, MediaStorageHandler mediaStorageHandler) {
        this.storageService = storageService;
        this.repository = repository;
        this.discoveryClient=discoveryClient;
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

    @PostMapping("/medias/{mediaId}/picture")
    public String handleFileUpload(@PathVariable Long mediaId, @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws ExternalServiceException {
        String loadImageEndpoint = mediaStorageHandler.saveMedia(file, mediaId);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        System.out.println(loadImageEndpoint);
        Media media = repository.findOne(mediaId);
        if(media==null){
            media= new Media();
            media.setContentUrl(loadImageEndpoint);
            media.setCreatedById(0);
            repository.save(media);
        } else {
            media.setContentUrl(loadImageEndpoint);
        }

        repository.save(media);

        return "redirect:/";
    }
}
