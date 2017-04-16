package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.TeamMedia;
import ba.steleks.repository.TeamsMediaJpaRepository;
import ba.steleks.service.Service;
import ba.steleks.service.discovery.ServiceDiscoveryClient;
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
public class TeamGalleryController {

    private final StorageService storageService;
    private final TeamsMediaJpaRepository repository;
    private final ServiceDiscoveryClient discoveryClient;

    @Autowired
    public TeamGalleryController(StorageService storageService, TeamsMediaJpaRepository repository, ServiceDiscoveryClient discoveryClient) {
        this.storageService = storageService;
        this.repository = repository;
        this.discoveryClient=discoveryClient;
    }

    @GetMapping("/teamPictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource("teamPictures/"+filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping("/teamMedia/{mediaId}/picture")
    public String handleFileUpload(@PathVariable Long mediaId, @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws ExternalServiceException {


        String mediaServiceBase = discoveryClient.getServiceUrl(Service.TEAMS);

        String[] names = file.getOriginalFilename().split("\\.");
        String dest = String.valueOf("teamPictures/" + mediaId + "_" + new Date().getTime()) + "." + names[names.length - 1];
        storageService.store(file, dest);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        TeamMedia teamMedia = repository.findOne(mediaId);

        teamMedia.setContentUrl(mediaServiceBase +"/"+ dest);

        repository.save(teamMedia);

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
