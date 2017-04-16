package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.User;
import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.service.Service;
import ba.steleks.service.discovery.ServiceDiscoveryClient;
import ba.steleks.storage.StorageFileNotFoundException;
import ba.steleks.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;
import java.util.Date;

/**
 * Created by admin on 02/04/2017.
 */
@RestController
public class ProfilePictureController {

    private final StorageService storageService;
    private final UsersJpaRepository repository;
    private final ServiceDiscoveryClient discoveryClient;

    @Autowired
    public ProfilePictureController(StorageService storageService, UsersJpaRepository repository, ServiceDiscoveryClient discoveryClient) {
        this.storageService = storageService;
        this.repository = repository;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/profilePictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource("profilePictures/"+ filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping("/users/{userId}/profilePicture")
    public String handleFileUpload(@PathVariable Long userId, @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws ExternalServiceException {

        String usersServiceBase = discoveryClient.getServiceUrl(Service.USERS);

        String[] names = file.getOriginalFilename().split("\\.");
        String dest = String.valueOf("profilePictures/" + userId + "_" + new Date().getTime()) + "." + names[names.length - 1];
        storageService.store(file, dest);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        User user = repository.findOne(userId);
        user.setProfilePictureUrl(usersServiceBase + "/" + dest);
        repository.save(user);

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
