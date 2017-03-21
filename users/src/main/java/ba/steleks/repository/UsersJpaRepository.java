package ba.steleks.repository;

/**
 * Created by ensar on 22/03/17.
 */

import ba.steleks.repository.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UsersJpaRepository extends PagingAndSortingRepository<User, Long> {

    List<User> findByUsername(@Param("username") String username);
    User findById(@Param("id") Long id);
}
