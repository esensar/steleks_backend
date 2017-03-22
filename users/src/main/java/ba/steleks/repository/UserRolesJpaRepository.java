package ba.steleks.repository;

/**
 * Created by ensar on 22/03/17.
 */

import ba.steleks.repository.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface UserRolesJpaRepository extends CrudRepository<User, Long> {
    List<User> findByUsername(@Param("username") String username);
}
