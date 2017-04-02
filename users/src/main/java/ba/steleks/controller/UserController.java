package ba.steleks.controller;

import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.repository.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

/**
 * Created by admin on 02/04/2017.
 */
@RepositoryRestController
public class UserController {

    @Autowired
    private UsersJpaRepository repository;

    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody User user) {
        System.out.println("doslo je do poziva");
        User result = repository.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).body(result);
    }
}
