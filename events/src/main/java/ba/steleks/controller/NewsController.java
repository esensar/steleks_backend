package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Event;
import ba.steleks.model.EventRequest;
import ba.steleks.repository.EventTypeJpaRepository;
import ba.steleks.repository.EventsJpaRepository;
import ba.steleks.repository.MediaJpaRepository;
import ba.steleks.util.ProxyHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.lang.Thread.sleep;

@RestController
public class NewsController {
    private static final long EVENT_TYPE_NEWS = 1;

    private EventsJpaRepository repository;
    private EventController eventController;

    @Autowired
    public NewsController(EventsJpaRepository repository,
                          EventController eventController) {
        this.repository = repository;
        this.eventController = eventController;
    }

    @RequestMapping(path = "/news", method = RequestMethod.POST)
    public ResponseEntity<?> addNews(@RequestBody EventRequest eventRequest, @RequestHeader(ProxyHeaders.USER_ID) String userId) throws ExternalServiceException {
        return eventController.addEventWithType(eventRequest, userId, EVENT_TYPE_NEWS);
    }

    @RequestMapping(path = "/news", method = RequestMethod.GET)
    public ResponseEntity<?> getNews() {
        Iterable<Event> result;
        result = repository.findByEventTypeId(EVENT_TYPE_NEWS);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Object() {
                    public Object _embedded = new Object() {
                        public Object events = result;
                    };
                });
    }
    @RequestMapping(path = "/ok", method = RequestMethod.POST)
    public ResponseEntity ok() throws InterruptedException {
        Thread.sleep(500);
        return ResponseEntity.status(HttpStatus.OK).body(' ');
    }
}
