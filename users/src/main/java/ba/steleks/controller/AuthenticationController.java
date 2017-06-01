package ba.steleks.controller;

import ba.steleks.error.exception.CustomHttpStatusException;
import ba.steleks.model.AuthRequest;
import ba.steleks.model.User;
import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.security.SessionIdentifierGenerator;
import ba.steleks.security.UserRoleFactory;
import ba.steleks.security.token.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 13/05/2017.
 */
@RestController
public class AuthenticationController {

    private UsersJpaRepository usersJpaRepository;
    private PasswordEncoder passwordEncoder;
    private TokenStore tokenStore;

    @Autowired
    public AuthenticationController(UsersJpaRepository usersJpaRepository, PasswordEncoder passwordEncoder, TokenStore tokenStore) {
        this.usersJpaRepository = usersJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
    }

    @RequestMapping(path = "/accesstoken", method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody AuthRequest body) {
        if(body.getUsername() == null || body.getPassword() == null) {
            throw new CustomHttpStatusException(HttpStatus.BAD_REQUEST,
                    "'username' and 'password' fields are mandatory!");
        }

        User user = usersJpaRepository.findByUsername(body.getUsername());
        if(user == null) {
            throw new CustomHttpStatusException(HttpStatus.NOT_FOUND,
                    "User with username " + body.getUsername() + " not found!");
        }

        if (passwordEncoder.matches(body.getPassword(), user.getPasswordHash())) {
            String token = new SessionIdentifierGenerator().nextSessionId();
            tokenStore.saveToken(user.getId(), token);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", String.valueOf(user.getId()));
            return ResponseEntity
                    .ok()
                    .body(response);
        } else {
            throw new CustomHttpStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid password!");
        }
    }

    @RequestMapping(path = "/accesstoken/{token}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeToken(@PathVariable String token) {
        tokenStore.removeToken(token);
        return ResponseEntity
                .noContent()
                .build();
    }

    @RequestMapping(path = "/accesstoken/{token}", method = RequestMethod.GET)
    public ResponseEntity<?> validateToken(@PathVariable String token) {
        if (tokenStore.isValidToken(token)) {
            Long userId = tokenStore.getTokenInfo(token);

            User user = usersJpaRepository.findOne(userId);
            if(user != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("userId", String.valueOf(userId));
                response.put("roles",
                        UserRoleFactory.toStringSet(user.getUserRoles())
                );
                return ResponseEntity
                        .ok()
                        .body(response);
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
            }
        } else {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }
}
