package ba.steleks.controller;/**
 * Created by ensar on 02/04/17.
 */

import ba.steleks.controller.storage.MediaStorageHandler;
import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.model.Media;
import ba.steleks.repository.MediaJpaRepository;
import ba.steleks.service.Service;
import ba.steleks.service.discovery.ServiceDiscoveryClient;
import org.apache.http.util.TextUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;

@RepositoryRestController
public class MediaController {

    private MediaJpaRepository repository;
    private RestTemplate restTemplate;
    private ServiceDiscoveryClient discoveryClient;
    private MediaStorageHandler mediaStorageHandler;

    @Autowired
    public MediaController(MediaJpaRepository repository, RestTemplateBuilder restTemplateBuilder, ServiceDiscoveryClient discoveryClient, MediaStorageHandler mediaStorageHandler) {
        this.repository = repository;
        this.restTemplate = restTemplateBuilder.build();
        this.discoveryClient = discoveryClient;
        this.mediaStorageHandler = mediaStorageHandler;
    }

    @RequestMapping(path = "/medias", method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody Media media) throws ExternalServiceException {

        String usersServiceBase = discoveryClient.getServiceUrl(Service.USERS);
        try {
//            String response = restTemplate.getForObject(usersServiceBase + "/users/{id}", String.class, media.getCreatedById());
            BufferedImage mediaImage = null;
            if (!TextUtils.isEmpty(media.getContentUrl())) {
                mediaImage = ImageIO.read(new URL(media.getContentUrl()));
            }
            Media result = repository.save(media);

            if (mediaImage != null) {
                String url = mediaStorageHandler.saveMedia(mediaImage, result.getId());
                result.setContentUrl(url);
                repository.save(result);
            }

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
