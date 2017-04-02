package ba.steleks.repository;

import ba.steleks.model.Media;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by admin on 23/03/2017.
 */

public interface MediaJpaRepository extends PagingAndSortingRepository<Media, Long> {

}
