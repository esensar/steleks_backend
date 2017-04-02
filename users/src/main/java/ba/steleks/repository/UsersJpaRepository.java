package ba.steleks.repository;

/**
 * Created by ensar on 22/03/17.
 */

import ba.steleks.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UsersJpaRepository extends PagingAndSortingRepository<User, Long> {

}

