package ba.steleks.repository;

/**
 * Created by ensar on 22/03/17.
 */

import ba.steleks.model.UserRole;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserRolesJpaRepository extends PagingAndSortingRepository<UserRole, Long> {
}
