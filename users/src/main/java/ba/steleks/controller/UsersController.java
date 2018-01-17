package ba.steleks.controller;

import ba.steleks.model.User;
import org.bouncycastle.crypto.generators.BCrypt;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.ws.rs.HttpMethod;

@RepositoryRestController
public class UsersController {



//    @RequestMapping(HttpMethod.POST)
//    public ResponseEntity<?> register(@RequestBody User newUser){
//        return null;
//    }

}
