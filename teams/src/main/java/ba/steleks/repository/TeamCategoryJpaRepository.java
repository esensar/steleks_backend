package ba.steleks.repository;

import ba.steleks.model.TeamCategory;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by admin on 24/03/2017.
 */
public interface TeamCategoryJpaRepository extends PagingAndSortingRepository<TeamCategory, Long> {
}
