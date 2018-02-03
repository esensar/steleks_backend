package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Event;
import ba.steleks.repository.EventsJpaRepository;
import ba.steleks.util.ProxyHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by admin on 01/04/2017.
 */

@RepositoryRestController
public class EventController {

    private EventsJpaRepository repository;

    @Autowired
    public EventController(EventsJpaRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(path = "/events", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Event event, @RequestHeader(ProxyHeaders.USER_ID) String userId) throws ExternalServiceException {
        try {
            event.setCreatedById(Long.parseLong(userId));
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

    @RequestMapping(path = "/events/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getEventsById(@RequestParam Long typeId) {
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
