/**
 * Created by admin on 22/03/2017.
 */
package ba.steleks.repository;

import ba.steleks.repository.model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;


public interface EventsJpaRepository extends PagingAndSortingRepository<Event, Long> {
}
