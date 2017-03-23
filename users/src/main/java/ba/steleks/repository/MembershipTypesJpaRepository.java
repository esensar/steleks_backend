package ba.steleks.repository;

/**
 * Created by ensar on 22/03/17.
 */

import ba.steleks.repository.model.MembershipType;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface MembershipTypesJpaRepository extends PagingAndSortingRepository<MembershipType, Long> {
}
