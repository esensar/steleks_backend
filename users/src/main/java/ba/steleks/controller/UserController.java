package ba.steleks.controller;

import ba.steleks.model.User;
import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.util.ProxyHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private UsersJpaRepository usersJpaRepository;

    @Autowired
    public UserController(UsersJpaRepository usersJpaRepository) {
        this.usersJpaRepository = usersJpaRepository;
    }

    @RequestMapping(path = "/users/current", method = RequestMethod.GET)
    public ResponseEntity<?> getCurrentUser(@RequestHeader(ProxyHeaders.USER_ID) String userIdString) {
        long userId = Long.parseLong(userIdString);
        User user = usersJpaRepository.findOne(userId);
        if (user != null) {
            System.out.println("Found user with id: " + userId);
            return ResponseEntity
                    .ok()
                    .body(user);
        } else {
            System.out.println("Found no user with id: " + userId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @RequestMapping(path = "/users/current", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateCurrentUser(@RequestHeader(ProxyHeaders.USER_ID) String userIdString, @RequestBody Map<String, Object> newUser) {
        long userId = Long.parseLong(userIdString);
        System.out.println("hepek");
        User user = usersJpaRepository.findOne(userId);
        if (user != null) {
            System.out.println("Found user with id: " + userId);
            if (newUser.get("firstName") != null) {
                user.setFirstName(newUser.get("firstName").toString());
            }
            if (newUser.get("lastName") != null) {
                user.setLastName(newUser.get("lastName").toString());
            }
            if (newUser.get("username") != null) {
                user.setUsername(newUser.get("username").toString());
            }
            if (newUser.get("contactNumber") != null) {
                user.setContactNumber(newUser.get("contactNumber").toString());
            }
            if (newUser.get("email") != null) {
                user.setEmail(newUser.get("email").toString());
            }
            usersJpaRepository.save(user);
            return ResponseEntity
                    .ok()
                    .body(user);
        } else {
            System.out.println("Found no user with id: " + userId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
