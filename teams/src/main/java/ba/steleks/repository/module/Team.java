package ba.steleks.repository.module;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.security.auth.Subject;

/**
 * Created by admin on 23/03/2017.
 */

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long subjectId;
    private int position;
    private double gradesOrPoints;
    private long contentId;
    private long eventId;

    protected Team() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getGradesOrPoints() {
        return gradesOrPoints;
    }

    public void setGradesOrPoints(double gradesOrPoints) {
        this.gradesOrPoints = gradesOrPoints;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }


}
