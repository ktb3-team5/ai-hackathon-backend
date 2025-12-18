package ktb.team5.ai.repository;

import ktb.team5.ai.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByMediaId(Long mediaId);

    List<Destination> findByIdIn(List<Long> destinationIds);
}
