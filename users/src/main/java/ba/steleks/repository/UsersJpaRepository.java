package ba.steleks.repository;

/**
 * Created by ensar on 22/03/17.
 */

import ba.steleks.repository.model.User;
import ba.steleks.repository.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

public interface UsersJpaRepository extends PagingAndSortingRepository<User, Long> {

}

