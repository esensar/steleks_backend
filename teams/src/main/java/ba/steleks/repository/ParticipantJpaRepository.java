package ba.steleks.repository;

import ba.steleks.repository.module.Participant;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by admin on 24/03/2017.
 */

public interface ParticipantJpaRepository extends PagingAndSortingRepository<Participant, Long> {
}
