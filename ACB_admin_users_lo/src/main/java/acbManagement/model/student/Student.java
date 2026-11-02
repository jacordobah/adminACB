package acbManagement.model.student;

import acbManagement.model.student.DTO.StudentRegistrationDto;
import acbManagement.model.user.DTO.UserRegistrationDto;
import acbManagement.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "student_id", referencedColumnName = "user_id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {

    public Student(UserRegistrationDto user, StudentRegistrationDto studentDto){
        this.setGrade(studentDto.grade());
        this.setSchoolId(studentDto.schoolId());
        this.setSection(studentDto.section());
        this.setSchoolShift(studentDto.schoolShift());
        this.setName(user.name());
        this.setAddress(user.address());
        this.setDocumentNumber(user.documentNumber());
        this.setDocumentType(user.documentType());
        this.setRole(user.role());
        this.setPhone(user.phone());
        this.setActive(true);
    }
    @Column(nullable = false, length = 20)
    private String grade;

    @Column(name = "school_shift", nullable = false, length = 50)
    private String schoolShift; // Jornada

    @Column(nullable = false)
    private String section;

    @Column(name = "school_id")
    private Long schoolId;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StudentGuardian> guardians = new ArrayList<>();
}
