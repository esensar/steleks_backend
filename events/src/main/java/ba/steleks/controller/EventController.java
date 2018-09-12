package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Event;
import ba.steleks.model.EventRequest;
import ba.steleks.model.Media;
import ba.steleks.repository.EventsJpaRepository;
import ba.steleks.repository.MediaJpaRepository;
import ba.steleks.util.ProxyHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 01/04/2017.
 */

@RepositoryRestController
public class EventController {

    private EventsJpaRepository repository;
    private MediaJpaRepository mediaJpaRepository;

    @Autowired
    public EventController(EventsJpaRepository repository, MediaJpaRepository mediaJpaRepository) {
        this.repository = repository;
        this.mediaJpaRepository = mediaJpaRepository;
    }

    @RequestMapping(path = "/events", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody EventRequest eventRequest, @RequestHeader(ProxyHeaders.USER_ID) String userId) throws ExternalServiceException {
        System.out.println(eventRequest);
        try {
            Event event = new Event();
            event.setTitle(eventRequest.getTitle());
            event.setShortText(eventRequest.getShortText());
            event.setLongText(eventRequest.getLongText());
            event.setCreatedById(Long.parseLong(userId));
            Set<Media> savedMedia = new HashSet<>();
            for (Media media : eventRequest.getMedias()) {
                Media loadedMedia = mediaJpaRepository.findOne(media.getId());
                savedMedia.add(loadedMedia);
                System.out.println("Loaded media: " + loadedMedia);
            }
            Event result = repository.save(event);
            result.setMediaSet(savedMedia);
            result = repository.save(result);
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
