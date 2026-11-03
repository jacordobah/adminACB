package acbManagement.repository;

import acbManagement.model.guide.GuideFaultCycle;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GuideFaultCycleRepository extends JpaRepository<GuideFaultCycle, Long> {
    Optional<GuideFaultCycle> findByGuideIdAndStatus(Long guideId, String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from GuideFaultCycle c where c.guideId = :guideId and c.status = 'OPEN'")
    Optional<GuideFaultCycle> findOpenCycleForGuideForUpdate(@Param("guideId") Long guideId);
}
