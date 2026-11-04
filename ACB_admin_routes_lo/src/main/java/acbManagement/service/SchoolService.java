package acbManagement.service;

import acbManagement.infra.exception.DuplicateResourceException;
import acbManagement.infra.exception.ResourceNotFoundException;
import acbManagement.model.school.DTO.SchoolRequest;
import acbManagement.model.school.DTO.SchoolResponse;
import acbManagement.model.school.School;
import acbManagement.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public List<SchoolResponse> findAll(String zone) {
        List<School> schools = (zone == null || zone.isBlank())
                ? schoolRepository.findByActiveTrue()
                : schoolRepository.findByZoneAndActiveTrue(zone);
        return schools.stream().map(this::toResponse).toList();
    }

    public SchoolResponse findById(Long id) {
        return toResponse(getActiveOrFail(id));
    }

    @Transactional
    public SchoolResponse create(SchoolRequest request) {
        if (schoolRepository.existsBySchoolCode(request.schoolCode())) {
            throw new DuplicateResourceException(
                    "Ya existe un colegio con el codigo " + request.schoolCode());
        }
        School school = new School();
        copyInto(school, request);
        school.setActive(true);
        return toResponse(schoolRepository.save(school));
    }

    @Transactional
    public SchoolResponse update(Long id, SchoolRequest request) {
        School school = getActiveOrFail(id);
        boolean codeChanged = !school.getSchoolCode().equals(request.schoolCode());
        if (codeChanged && schoolRepository.existsBySchoolCode(request.schoolCode())) {
            throw new DuplicateResourceException(
                    "Ya existe un colegio con el codigo " + request.schoolCode());
        }
        copyInto(school, request);
        return toResponse(schoolRepository.save(school));
    }

    // Baja logica: el colegio queda inactivo pero no se borra,
    // porque las rutas y los recorridos ya ejecutados lo referencian
    @Transactional
    public void deactivate(Long id) {
        School school = getActiveOrFail(id);
        school.setActive(false);
        schoolRepository.save(school);
    }

    private School getActiveOrFail(Long id) {
        return schoolRepository.findById(id)
                .filter(s -> Boolean.TRUE.equals(s.getActive()))
                .orElseThrow(() -> new ResourceNotFoundException("No existe el colegio " + id));
    }

    private void copyInto(School school, SchoolRequest request) {
        school.setName(request.name());
        school.setSchoolCode(request.schoolCode());
        school.setPhone(request.phone());
        school.setAddress(request.address());
        school.setZone(request.zone());
        school.setLatitude(request.latitude());
        school.setLongitude(request.longitude());
    }

    private SchoolResponse toResponse(School s) {
        return new SchoolResponse(
                s.getSchoolId(),
                s.getName(),
                s.getSchoolCode(),
                s.getPhone(),
                s.getAddress(),
                s.getZone(),
                s.getLatitude(),
                s.getLongitude(),
                s.getActive()
        );
    }
}
