package ba.steleks.model;

import com.sun.istack.internal.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by admin on 23/03/2017.
 */

@Entity
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String contentUrl;
    @Column(updatable = false, insertable = false)
    private Timestamp creationDate;
    @Nullable
    private long createdById;

    public Media() {
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

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(long createdById) {
        this.createdById = createdById;
    }

    @PrePersist
    @PreUpdate
    protected void onCreate() {
        this.creationDate = new Timestamp(new Date().getTime());
    }

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", contentUrl='" + contentUrl + '\'' +
                ", creationDate=" + creationDate +
                ", createdById=" + createdById +
                '}';
    }
}
