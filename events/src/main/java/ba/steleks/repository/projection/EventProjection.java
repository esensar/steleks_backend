package ba.steleks.repository.projection;/**
 * Created by ensar on 10/06/17.
 */

import ba.steleks.model.Event;
import ba.steleks.model.EventType;
import ba.steleks.model.Media;
import org.springframework.data.rest.core.config.Projection;

import java.sql.Timestamp;
import java.util.Set;

@Projection(name = "eventProjection", types = { Event.class })
public interface EventProjection {
    Integer getId();
    String getTitle();
    String getShortText();
    String getLongText();
    Timestamp getDateTime();
    int getDuration();
    long getCreatedById();
    EventType getEventType();
    Set<Media> getMediaSet();
}