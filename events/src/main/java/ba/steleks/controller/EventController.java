package ba.steleks.controller;

import ba.steleks.service.Service;
import ba.steleks.service.discovery.ServiceDiscoveryClient;
import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.repository.EventsJpaRepository;
import ba.steleks.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.ws.rs.GET;
import java.net.URI;
import java.util.List;

/**
 * Created by admin on 01/04/2017.
 */

@RepositoryRestController
public class EventController {

    private EventsJpaRepository repository;

    private RestTemplate restTemplate;

    private ServiceDiscoveryClient discoveryClient;

    @Autowired
    public EventController(EventsJpaRepository repository, RestTemplateBuilder restTemplateBuilder, ServiceDiscoveryClient discoveryClient) {
        this.repository = repository;
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping(path = "/events", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Event event) throws ExternalServiceException {
        String usersServiceBase = discoveryClient.getServiceUrl(Service.USERS);
        try {
            String response = restTemplate.getForObject(usersServiceBase + "/users/{id}", String.class, event.getCreatedById());
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

    @RequestMapping(path = "/events", method = RequestMethod.GET)
    public ResponseEntity<?> getEventsById(@RequestParam(required = false) Long typeId) {
        Iterable<Event> result;
        if (typeId == null) {
            result = repository.findAll();
        } else {
            result = repository.findByEventTypeId(typeId);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Object() {
                    public Object _embedded = new Object() {
                        public Object events = result;
                    };
                });
    }

}
