package acbManagement.repository;

import acbManagement.model.guide.MajorFault;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MajorFaultRepository extends JpaRepository<MajorFault, Long> {
    List<MajorFault> findAllByTripGuideAttendance_TripGuideIdOrderByOpenedAtDesc(Long tripGuideId);

    Optional<MajorFault> findByCycle_CycleId(Long cycleId);

    List<MajorFault> findAllByStatusOrderByOpenedAtDesc(String status);
}
