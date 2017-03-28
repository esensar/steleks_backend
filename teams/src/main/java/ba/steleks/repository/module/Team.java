package ba.steleks.repository.module;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.util.Set;

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

    @ManyToOne
    @JoinColumn
    private TeamCategory teamCategory;

    private long eventId;

    @ManyToMany
    @JoinColumn
    private Set<Participant> participantSet;

    @ManyToMany
    @JoinColumn
    private Set<TeamMedia> teamMediaSet;

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

    public TeamCategory getContentId() {
        return teamCategory;
    }

    public void setContentId(TeamCategory contentId) {
        this.teamCategory = contentId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Set<Participant> getParticipantSet() {
        return participantSet;
    }

    public void setParticipantSet(Set<Participant> participantSet) {
        this.participantSet = participantSet;
    }

    public TeamCategory getTeamCategory() {
        return teamCategory;
    }

    public void setTeamCategory(TeamCategory teamCategory) {
        this.teamCategory = teamCategory;
    }

    public Set<TeamMedia> getTeamMediaSet() {
        return teamMediaSet;
    }

    public void setTeamMediaSet(Set<TeamMedia> teamMediaSet) {
        this.teamMediaSet = teamMediaSet;
    }
}
