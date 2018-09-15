package ba.steleks.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    @Value("${default.storage.directory}")
    private String location;

    public String getLocation() {
        return "/Users/ensar.sarajcic/Steleks";
    }

    public void setLocation(String location) {
        this.location = location;
    }

}