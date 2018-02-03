package ba.steleks.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by admin on 23/03/2017.
 */
@Entity
public class TeamMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String contentUrl;


    protected TeamMedia() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
