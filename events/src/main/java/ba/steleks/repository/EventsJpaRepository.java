/**
 * Created by admin on 22/03/2017.
 */
package ba.steleks.repository;

import ba.steleks.repository.model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface EventsJpaRepository extends PagingAndSortingRepository<Event, Long> {

    Event save(Event event);
    List<Event> findAll();
    Event findOne(Long ID);

}
