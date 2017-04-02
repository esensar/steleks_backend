package ba.steleks.repository;

import ba.steleks.model.TeamMedia;
import org.springframework.data.repository.PagingAndSortingRepository;


/**
 * Created by admin on 24/03/2017.
 */
public interface TeamsMediaJpaRepository extends PagingAndSortingRepository<TeamMedia, Long> {
}
