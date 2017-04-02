/**
 * Created by admin on 22/03/2017.
 */
package ba.steleks.repository;

import ba.steleks.model.Event;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface EventsJpaRepository extends PagingAndSortingRepository<Event, Long> {
}
