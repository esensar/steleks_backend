package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.repository.EventsJpaRepository;
import ba.steleks.model.Event;
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

/**
 * Created by admin on 01/04/2017.
 */

@RepositoryRestController
public class EventController {

    private EventsJpaRepository repository;

    private RestTemplate restTemplate;

    @Autowired
    public EventController(RestTemplateBuilder restTemplateBuilder, EventsJpaRepository repository) {
        this.restTemplate = restTemplateBuilder.build();
        this.repository = repository;
    }

    @RequestMapping(path = "/events", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Event event) throws ExternalServiceException {

        String oviUseriNeki = "http://localhost:8090/users/{id}";
        try {

            String response = restTemplate.getForObject(oviUseriNeki, String.class, event.getCreatedById());
            Event result = repository.save(event);
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
