package acbManagement.repository;

import acbManagement.model.guide.MinorFault;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MinorFaultRepository extends JpaRepository<MinorFault, Long> {
    List<MinorFault> findAllByCycle_CycleIdOrderBySequenceInCycle(Long cycleId);

    Optional<MinorFault> findByCycle_CycleIdAndSequenceInCycle(Long cycleId, Short sequenceInCycle);

    long countByCycle_CycleId(Long cycleId);
}
