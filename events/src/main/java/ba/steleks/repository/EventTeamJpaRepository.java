package ba.steleks.repository;

import ba.steleks.model.EventTeam;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by admin on 15/04/2017.
 */
public interface EventTeamJpaRepository extends PagingAndSortingRepository<EventTeam, Long> {
}
