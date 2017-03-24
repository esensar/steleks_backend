package ba.steleks.repository;

import ba.steleks.repository.model.EventType;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by admin on 24/03/2017.
 */
public interface EventTypeJpaRepository extends PagingAndSortingRepository<EventType, Long> {
}
