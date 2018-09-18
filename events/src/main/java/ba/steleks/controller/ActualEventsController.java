package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Event;
import ba.steleks.model.EventRequest;
import ba.steleks.repository.EventTypeJpaRepository;
import ba.steleks.repository.EventsJpaRepository;
import ba.steleks.repository.MediaJpaRepository;
import ba.steleks.util.ProxyHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.awt.print.Pageable;

@RestController
public class ActualEventsController {
    private static final long EVENT_TYPE_EVENT = 2;

    private EventsJpaRepository repository;
    private EventController eventController;

    @Autowired
    public ActualEventsController(EventsJpaRepository repository, EventController eventController) {
        this.repository = repository;
        this.eventController = eventController;
    }

    @RequestMapping(path = "/realEvents", method = RequestMethod.GET)
    public ResponseEntity<?> getRealEvents(
            @RequestParam( "page" ) int page,
            @RequestParam( "size" ) int size) {
        Iterable<Event> result;
        result = repository.findAllByEventTypeId(EVENT_TYPE_EVENT, new PageRequest(page, size));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Object() {
                    public Object _embedded = new Object() {
                        public Object events = result;
                    };
                });
    }


    @RequestMapping(path = "/realEvents", method = RequestMethod.POST)
    public ResponseEntity<?> addEvents(
            @RequestBody EventRequest eventRequest,
            @RequestHeader(ProxyHeaders.USER_ID) String userId) throws ExternalServiceException {
        return eventController.addEventWithType(eventRequest, userId, EVENT_TYPE_EVENT);
    }
}
