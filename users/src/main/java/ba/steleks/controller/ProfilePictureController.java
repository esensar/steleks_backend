package ba.steleks.controller;

import ba.steleks.model.User;
import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.storage.StorageFileNotFoundException;
import ba.steleks.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
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

    @Autowired
    public ProfilePictureController(StorageService storageService, UsersJpaRepository repository) {
        this.storageService = storageService;
        this.repository = repository;
    }

    @GetMapping("/profilePictures/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @PostMapping("/users/{userId}/profilePicture")
    public String handleFileUpload(@PathVariable Long userId, @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        String[] names = file.getOriginalFilename().split("\\.");
        String dest = String.valueOf("profilePictures/" + userId + "_" + new Date().getTime()) + "." + names[names.length - 1];
        storageService.store(file, dest);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        User user = repository.findOne(userId);
        user.setProfilePictureUrl("http://localhost:8090" + dest);
        repository.save(user);

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
