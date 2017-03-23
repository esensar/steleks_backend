/**
 * Created by admin on 22/03/2017.
 */
package ba.steleks.repository;

import ba.steleks.repository.model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "events", path = "events")
public interface EventsJpaRepository extends PagingAndSortingRepository<Event, Long> {

    Event save(Event event);
    List<Event> findAll();
    Event findOne(Long ID);
    List<Event> findByTitle(String title);

}
