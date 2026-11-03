package acbManagement.service;

import acbManagement.model.guide.GuideFaultCycle;
import acbManagement.model.guide.MajorFault;
import acbManagement.model.guide.MinorFault;
import acbManagement.model.trip.TripGuideAttendance;
import acbManagement.repository.GuideFaultCycleRepository;
import acbManagement.repository.MajorFaultRepository;
import acbManagement.repository.MinorFaultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuideFaultService {
    private static final ZoneId BUSINESS_ZONE = ZoneId.of("America/Bogota");

    private final GuideFaultCycleRepository cycleRepository;
    private final MinorFaultRepository minorFaultRepository;
    private final MajorFaultRepository majorFaultRepository;

    @Transactional(readOnly = true)
    public List<GuideFaultCycle> listCyclesForGuide(Long guideId) {
        return cycleRepository.findAllByGuideIdOrderByOpenedAtDesc(guideId);
    }

    @Transactional(readOnly = true)
    public List<MinorFault> listMinorFaults(Long cycleId) {
        if (!cycleRepository.existsById(cycleId)) {
            throw OperationServiceSupport.notFound("No existe el ciclo de faltas " + cycleId);
        }
        return minorFaultRepository.findAllByCycle_CycleIdOrderBySequenceInCycle(cycleId);
    }

    @Transactional(readOnly = true)
    public MajorFault getMajorFault(Long majorFaultId) {
        return majorFaultRepository.findById(majorFaultId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe la falta grave " + majorFaultId));
    }

    /**
     * Se llama solo cuando el check-in fue aceptado geográficamente.
     * Llegar después del límite de 15 minutos, hasta la hora programada inclusive: leve.
     * Llegar después de la hora programada: grave directa.
     */
    @Transactional
    public void registerArrivalFault(TripGuideAttendance tripGuide, OffsetDateTime arrivedAt) {
        OffsetDateTime scheduledStart = tripGuide.getTrip().getTripDate()
                .atTime(tripGuide.getTrip().getScheduledStartSnapshot())
                .atZone(BUSINESS_ZONE)
                .toOffsetDateTime();
        OffsetDateTime minorDeadline = scheduledStart.minusMinutes(15);

        if (arrivedAt.isAfter(scheduledStart)) {
            registerDirectMajorFault(tripGuide, arrivedAt);
        } else if (arrivedAt.isAfter(minorDeadline)) {
            registerMinorFault(tripGuide, arrivedAt);
        }
    }

    @Transactional
    public MinorFault markMinorFaultNotified(Long minorFaultId, Long leaderUserId) {
        MinorFault fault = minorFaultRepository.findById(minorFaultId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe la falta leve " + minorFaultId));
        if (!"PENDING".equals(fault.getStatus())) {
            throw OperationServiceSupport.conflict("La falta leve ya fue notificada.");
        }
        fault.setStatus("NOTIFIED");
        fault.setManagedByUserId(leaderUserId);
        fault.setNotifiedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return minorFaultRepository.save(fault);
    }

    @Transactional
    public MajorFault markMajorFaultNotified(Long majorFaultId, Long managerUserId) {
        MajorFault fault = majorFaultRepository.findById(majorFaultId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe la falta grave " + majorFaultId));
        if (!"OPEN".equals(fault.getStatus())) {
            throw OperationServiceSupport.conflict("Solo una falta grave OPEN puede pasar a NOTIFIED.");
        }
        fault.setStatus("NOTIFIED");
        fault.setManagedByUserId(managerUserId);
        fault.setNotifiedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return majorFaultRepository.save(fault);
    }

    @Transactional
    public MajorFault markMajorFaultMinuted(Long majorFaultId) {
        MajorFault fault = majorFaultRepository.findById(majorFaultId)
                .orElseThrow(() -> OperationServiceSupport.notFound("No existe la falta grave " + majorFaultId));
        if (!"NOTIFIED".equals(fault.getStatus())) {
            throw OperationServiceSupport.conflict("La falta grave debe estar NOTIFIED antes de cerrarse.");
        }
        fault.setStatus("MINUTED");
        fault.setClosedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return majorFaultRepository.save(fault);
    }

    private void registerMinorFault(TripGuideAttendance tripGuide, OffsetDateTime occurredAt) {
        Long guideId = resolveActualGuideId(tripGuide);
        GuideFaultCycle cycle = cycleRepository.findOpenCycleForGuideForUpdate(guideId)
                .orElseGet(() -> {
                    GuideFaultCycle created = new GuideFaultCycle();
                    created.setGuideId(guideId);
                    created.setStatus("OPEN");
                    created.setOpenedAt(OffsetDateTime.now(ZoneOffset.UTC));
                    return cycleRepository.saveAndFlush(created);
                });

        long count = minorFaultRepository.countByCycle_CycleId(cycle.getCycleId());
        if (count >= 5) {
            throw OperationServiceSupport.conflict("El ciclo ya tiene cinco faltas leves y debe convertirse.");
        }

        short sequence = (short) (count + 1);
        MinorFault fault = new MinorFault();
        fault.setCycle(cycle);
        fault.setTripGuideAttendance(tripGuide);
        fault.setSequenceInCycle(sequence);
        fault.setReason("Llegada posterior al límite de 15 minutos antes del inicio.");
        fault.setStatus("PENDING");
        fault.setNotificationType(notificationTypeFor(sequence));
        fault.setRecordedAt(occurredAt);
        minorFaultRepository.save(fault);

        if (sequence == 5) {
            MajorFault major = new MajorFault();
            major.setTripGuideAttendance(tripGuide);
            major.setCycle(cycle);
            major.setOrigin("FIVE_MINORS");
            major.setStatus("OPEN");
            major.setOpenedAt(occurredAt);
            majorFaultRepository.save(major);

            cycle.setStatus("CONVERTED");
            cycle.setClosedAt(occurredAt);
            cycleRepository.save(cycle);
        }
    }

    private void registerDirectMajorFault(TripGuideAttendance tripGuide, OffsetDateTime occurredAt) {
        MajorFault major = new MajorFault();
        major.setTripGuideAttendance(tripGuide);
        major.setCycle(null);
        major.setOrigin("DIRECT");
        major.setStatus("OPEN");
        major.setOpenedAt(occurredAt);
        majorFaultRepository.save(major);
    }

    private static String notificationTypeFor(short sequence) {
        if (sequence <= 2) return "VERBAL";
        if (sequence <= 4) return "EMAIL";
        return "REQUIREMENT";
    }

    private static Long resolveActualGuideId(TripGuideAttendance tripGuide) {
        return tripGuide.getActualGuideId() != null
                ? tripGuide.getActualGuideId()
                : tripGuide.getPlannedGuideId();
    }
}
