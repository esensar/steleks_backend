package ba.steleks.controller;

import ba.steleks.model.AuthRequest;
import ba.steleks.repository.UsersJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 13/05/2017.
 */
@RestController
public class AuthenticationController {

    private UsersJpaRepository usersJpaRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(UsersJpaRepository usersJpaRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.usersJpaRepository = usersJpaRepository;
    }

    @RequestMapping(path = "/accesstoken", method = RequestMethod.POST)
    public String generateToken(@RequestBody AuthRequest body){
        return passwordEncoder.matches(body.getPassword(), usersJpaRepository.findByUsername(body.getUsername()).getPasswordHash()) ? "true" : "false";
    }

}
