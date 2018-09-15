package ba.steleks.controller;

import ba.steleks.model.User;
import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.util.ProxyHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
