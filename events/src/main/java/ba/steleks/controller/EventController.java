package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Event;
import ba.steleks.model.EventRequest;
import ba.steleks.model.EventType;
import ba.steleks.model.Media;
import ba.steleks.repository.EventTypeJpaRepository;
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
    private EventTypeJpaRepository eventTypeJpaRepository;

    @Autowired
    public EventController(EventsJpaRepository repository,
                           MediaJpaRepository mediaJpaRepository,
                           EventTypeJpaRepository eventTypeJpaRepository) {
        this.repository = repository;
        this.mediaJpaRepository = mediaJpaRepository;
        this.eventTypeJpaRepository = eventTypeJpaRepository;
    }

    public ResponseEntity<?> addEventWithType(EventRequest eventRequest, String userId, long eventTypeId) throws ExternalServiceException {
        EventType eventType = eventTypeJpaRepository.findOne(eventTypeId);
        System.out.println(eventRequest);
        try {
            Event event = new Event();
            event.setTitle(eventRequest.getTitle());
            event.setShortText(eventRequest.getShortText());
            event.setLongText(eventRequest.getLongText());
            event.setCreatedById(Long.parseLong(userId));
            event.setEventType(eventType);
            Set<Media> savedMedia = new HashSet<>();
            if (eventRequest.getMedias() != null) {
                for (Media media : eventRequest.getMedias()) {
                    Media loadedMedia = mediaJpaRepository.findOne(media.getId());
                    savedMedia.add(loadedMedia);
                    System.out.println("Loaded media: " + loadedMedia);
                }
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
    public ResponseEntity<?> getEventsById(@PathVariable("id") String eventId) {
        Event result;
        result = repository.findOne(Long.parseLong(eventId));
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }
}
