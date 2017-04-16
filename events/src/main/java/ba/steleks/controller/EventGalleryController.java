package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Media;
import ba.steleks.repository.EventsJpaRepository;
import ba.steleks.repository.MediaJpaRepository;
import ba.steleks.storage.StorageFileNotFoundException;
import ba.steleks.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

/**
 * Created by admin on 16/04/2017.
 */
@RestController
public class EventGalleryController {

    private final StorageService storageService;
    private final MediaJpaRepository repository;
    private final DiscoveryClient discoveryClient;

    @Autowired
    public EventGalleryController(StorageService storageService, MediaJpaRepository repository, DiscoveryClient discoveryClient) {
        this.storageService = storageService;
        this.repository = repository;
        this.discoveryClient=discoveryClient;
    }

    @GetMapping("/eventPictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource("mediaEvent/"+filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping("/media/{mediaId}/picture")
    public String handleFileUpload(@PathVariable Long mediaId, @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws ExternalServiceException {

        List<ServiceInstance> usersInstances = discoveryClient.getInstances("events");
        if(usersInstances == null || usersInstances.size() == 0) {
            System.err.print("Users service not found!");
            throw new ExternalServiceException();
        }

        ServiceInstance usersService = usersInstances.get(0);
        String mediaServiceBase = usersService.getUri().toString();

        String[] names = file.getOriginalFilename().split("\\.");
        String dest = String.valueOf("mediaEvent/" + mediaId + "_" + new Date().getTime()) + "." + names[names.length - 1];
        storageService.store(file, dest);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        Media media = repository.findOne(mediaId);
        media.setContentUrl(mediaServiceBase+"/profilePictures/" + dest);

        repository.save(media);

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
