package ba.steleks.controller.storage;

import ba.steleks.error.exception.ExternalServiceException;
import ba.steleks.service.Service;
import ba.steleks.service.discovery.ServiceDiscoveryClient;
import ba.steleks.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

@Component
public class MediaStorageHandler {

    private StorageService storageService;
    private ServiceDiscoveryClient discoveryClient;

    @Autowired
    public MediaStorageHandler(StorageService storageService, ServiceDiscoveryClient discoveryClient) {
        this.storageService = storageService;
        this.discoveryClient = discoveryClient;
    }

    public String saveMedia(MultipartFile file, long mediaId) throws ExternalServiceException {
        String mediaServiceBase = discoveryClient.getServiceUrl(Service.EVENTS);
        System.out.println(mediaServiceBase);
        String[] names = file.getOriginalFilename().split("\\.");
        String dest = String.valueOf("eventPictures/" + mediaId + "_" + new Date().getTime()) + "." + names[names.length - 1];
        storageService.store(file, dest);
        return mediaServiceBase + "/" + dest;
    }

    public String saveMedia(BufferedImage bufferedImage, long mediaId) throws ExternalServiceException {
        try {
            String mediaServiceBase = discoveryClient.getServiceUrl(Service.EVENTS);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            baos.flush();
            String dest = String.valueOf("eventPictures/" + mediaId + "_" + new Date().getTime()) + ".jpg";
            storageService.store(new ByteArrayResource(baos.toByteArray()), dest);
            baos.close();
            return mediaServiceBase + "/" + dest;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
