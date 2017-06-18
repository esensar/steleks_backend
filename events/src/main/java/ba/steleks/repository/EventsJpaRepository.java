/**
 * Created by admin on 22/03/2017.
 */
package ba.steleks.repository;

import ba.steleks.model.Event;
import ba.steleks.repository.projection.EventProjection;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(excerptProjection = EventProjection.class)
public interface EventsJpaRepository extends PagingAndSortingRepository<Event, Long> {
    List<Event> findByEventTypeId(Long eventTypeId);
}
