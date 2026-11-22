package acbManagement.controller;

import acbManagement.infra.securiity.CurrentActor;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody @Valid StudentRequest request,
                                                 @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        Student newStudent = studentService.registerCompleteStudent(request.user(), request.student(), actor);
        return new ResponseEntity<>(newStudent, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/guardians")
    public ResponseEntity<Void> assignGuardian(@PathVariable Long id, @RequestBody @Valid AssignGuardianDto dto,
                                               @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        studentService.assignGuardianToStudent(id, dto, actor);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<StudentDetailResponse>> getAllStudents(Pageable pageable,
                                                                      @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.getAllStudentsPaged(pageable, actor));
    }

    @GetMapping("/school/{schoolId}")
    public ResponseEntity<Page<StudentDetailResponse>> getStudentsBySchool(@PathVariable Long schoolId, Pageable pageable,
                                                                           @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.getStudentsBySchoolPaged(schoolId, pageable, actor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailResponse> getStudentById(@PathVariable Long id,
                                                                @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.getStudentById(id, actor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDetailResponse> updateStudent(@PathVariable Long id, @RequestBody StudentRegistrationDto dto,
                                                               @AuthenticationPrincipal Jwt jwt) {

        CurrentActor actor = CurrentActor.from(jwt);
        actor.requireAnyRole("GUIDE","LEADER","MANAGER");
        return ResponseEntity.ok(studentService.updateStudent(id, dto, actor));
    }
}
