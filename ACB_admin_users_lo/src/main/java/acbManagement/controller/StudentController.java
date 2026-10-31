package acbManagement.controller;

import acbManagement.model.student.DTO.AssignGuardianDto;
import acbManagement.model.student.DTO.StudentDetailResponse;
import acbManagement.model.student.DTO.StudentRegistrationDto;
import acbManagement.model.student.Student;
import acbManagement.model.user.DTO.StudentRequest;
import acbManagement.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody @Valid StudentRequest request) {
        Student newStudent = studentService.registerCompleteStudent(request.user(), request.student());
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/guardians")
    public ResponseEntity<Void> assignGuardian(@PathVariable Long id, @RequestBody @Valid AssignGuardianDto dto) {
        studentService.assignGuardianToStudent(id, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<StudentDetailResponse>> getAllStudents(Pageable pageable) {
        return ResponseEntity.ok(studentService.getAllStudentsPaged(pageable));
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<Page<StudentDetailResponse>> getStudentsBySchool(@PathVariable Long schoolId, Pageable pageable) {
        return ResponseEntity.ok(studentService.getStudentsBySchoolPaged(schoolId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDetailResponse> updateStudent(@PathVariable Long id, @RequestBody StudentRegistrationDto dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }
}
