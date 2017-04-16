package ba.steleks.controller;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Event;
import ba.steleks.model.EventTeam;
import ba.steleks.repository.EventTeamJpaRepository;
import ba.steleks.repository.EventsJpaRepository;
import ba.steleks.service.Service;
import ba.steleks.service.discovery.ServiceDiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by admin on 15/04/2017.
 */

@RepositoryRestController
public class EventTeamController {

    private EventTeamJpaRepository repository;

    private RestTemplate restTemplate;

    private ServiceDiscoveryClient discoveryClient;

    @Autowired
    public EventTeamController(EventTeamJpaRepository repository, RestTemplateBuilder restTemplateBuilder, ServiceDiscoveryClient discoveryClient) {
        this.repository = repository;
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
    }

    @RequestMapping(path = "/eventTeams", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody EventTeam eventTeam) throws ExternalServiceException {

        String teamsServiceBase = discoveryClient.getServiceUrl(Service.TEAMS);
        try {

            String response = restTemplate.getForObject(teamsServiceBase + "/teams/{id}", String.class, eventTeam.getTeamId());
            EventTeam result = repository.save(eventTeam);
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
