package ba.steleks.repository;

import ba.steleks.model.Team;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by admin on 23/03/2017.
 */

public interface TeamsJpaRepository extends PagingAndSortingRepository<Team, Long> {
}
