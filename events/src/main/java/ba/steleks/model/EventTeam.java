package ba.steleks.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by admin on 15/04/2017.
 */

@Entity
public class EventTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn
    private Event eventId;

    private long teamId;

    public EventTeam() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long team_id) {
        this.teamId = team_id;
    }

    public Event getEventSet() {
        return eventId;
    }

    public void setEventSet(Event eventSet) {
        this.eventId = eventSet;
    }
}
