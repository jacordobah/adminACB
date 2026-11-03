package acbManagement.model.trip;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "trip_student_attendance")
@Getter
@Setter
@NoArgsConstructor
public class TripStudentAttendance {
    @EmbeddedId
    private TripStudentAttendanceId id;

    @MapsId("tripId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @Column(name = "student_name_snapshot", nullable = false, length = 150)
    private String studentNameSnapshot;

    @Column(name = "status_code", nullable = false, length = 2)
    private String statusCode; // 00, 01, 0I o 0C

    @Column(name = "note")
    private String note;

    @Column(name = "accompanying_person_name", length = 150)
    private String accompanyingPersonName;

    @Column(name = "accompanying_person_relation", length = 100)
    private String accompanyingPersonRelation;

    // ID externo del usuario autenticado que registró la asistencia.
    @Column(name = "recorded_by_user_id")
    private Long recordedByUserId;

    @Column(name = "recorded_at", nullable = false)
    private OffsetDateTime recordedAt;

    @PrePersist
    void setRecordedAtIfMissing() {
        if (recordedAt == null) recordedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
