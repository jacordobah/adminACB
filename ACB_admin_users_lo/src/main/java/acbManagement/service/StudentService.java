package acbManagement.service;

import acbManagement.model.student.DTO.AssignGuardianDto;
import acbManagement.model.student.DTO.GuardianDetailResponse;
import acbManagement.model.student.DTO.StudentDetailResponse;
import acbManagement.model.student.DTO.StudentRegistrationDto;
import acbManagement.model.student.Student;
import acbManagement.model.student.StudentGuardian;
import acbManagement.model.user.DTO.UserRegistrationDto;
import acbManagement.model.user.Role;
import acbManagement.model.user.User;
import acbManagement.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final UserService userService;
    private final StudentRepository studentRepository;

    @Transactional
    public Student registerCompleteStudent(UserRegistrationDto userDto, StudentRegistrationDto studentDto) {
        // FLUJO OBLIGATORIO 1: Registrar el usuario base primero
        Student student = new Student(userDto,studentDto);
        return studentRepository.save(student);
    }

    @Transactional
    public void assignGuardianToStudent(Long studentId, AssignGuardianDto dto) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        User guardian = userService.findById(dto.guardianId());

        if (guardian.getRole() != Role.GUARDIAN) {
            throw new IllegalArgumentException("El usuario asignado no tiene rol de ACUDIENTE");
        }

        StudentGuardian studentGuardian = new StudentGuardian();
        studentGuardian.setStudent(student);
        studentGuardian.setGuardian(guardian);
        studentGuardian.setRelationship(dto.relationship());
        studentGuardian.setPrimary(dto.isPrimary());

        student.getGuardians().add(studentGuardian);
        studentRepository.save(student);
    }

    public Page<StudentDetailResponse> getAllStudentsPaged(Pageable pageable) {
        return studentRepository.findByActiveTrue(pageable).map(this::convertToDetailResponse);
    }

    public Page<StudentDetailResponse> getStudentsBySchoolPaged(Long schoolId, Pageable pageable) {
        return studentRepository.findBySchoolIdAndActiveTrue(schoolId, pageable).map(this::convertToDetailResponse);
    }

    public StudentDetailResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .filter(s -> s.isActive())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        return convertToDetailResponse(student);
    }

    @Transactional
    public StudentDetailResponse updateStudent(Long id, StudentRegistrationDto dto) {
        Student student = studentRepository.findById(id)
                .filter(s -> s.isActive())
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        student.setGrade(dto.grade());
        student.setSchoolShift(dto.schoolShift());
        student.setSection(dto.section());

        return convertToDetailResponse(studentRepository.save(student));
    }

    public StudentDetailResponse convertToDetailResponse(Student student) {
        List<GuardianDetailResponse> guardians = student.getGuardians().stream()
                .map(sg -> new GuardianDetailResponse(
                        sg.getGuardian().getUserId(),
                        sg.getGuardian().getName(),
                        sg.getGuardian().getPhone(),
                        sg.getRelationship(),
                        sg.isPrimary()
                )).toList();

        return new StudentDetailResponse(
                student.getUserId(),
                student.getDocumentType().name(),
                student.getDocumentNumber(),
                student.getName(),
                student.getAddress(),
                student.getPhone(),
                student.getGrade(),
                student.getSchoolShift(),
                student.getSection(),
                student.getSchoolId(),
                guardians
        );
    }
}
