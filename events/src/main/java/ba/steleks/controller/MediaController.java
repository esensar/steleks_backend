package ba.steleks.controller;/**
 * Created by ensar on 02/04/17.
 */

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Event;
import ba.steleks.model.Media;
import ba.steleks.repository.MediaJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RepositoryRestController
public class MediaController {

    private MediaJpaRepository repository;
    private RestTemplate restTemplate;

    @Autowired
    public MediaController(RestTemplateBuilder restTemplateBuilder, MediaJpaRepository repository) {
        this.repository = repository;
        this.restTemplate = restTemplateBuilder.build();
    }

    @RequestMapping(path = "/medias", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Media media) throws ExternalServiceException {

        String oviUseriNeki = "http://localhost:8090/users/{id}";
        try {
            String response = restTemplate.getForObject(oviUseriNeki, String.class, media.getCreatedById());
            Media result = repository.save(media);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{id}")
                    .buildAndExpand(result.getId()).toUri();
            return ResponseEntity.created(location).body(result);
        } catch (Exception ex) {
            if (ex instanceof HttpClientErrorException && ((HttpClientErrorException) ex).getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            } else {
                throw new ExternalServiceException();
            }
        }
    }
}
