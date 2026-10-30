package acbManagement.model.trip;

import acbManagement.model.event.ActionCode;
import acbManagement.model.guide.Guide;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_events") // En plural
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guide_id", nullable = false)
    private Guide guide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_id", nullable = false)
    private ActionCode actionCode;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime; // TIMESTAMP del reporte

    @Column(name = "latitude")
    private Double latitude; // Nullable por si es un reporte manual de novedad central

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations; // Detalles extras informados por el guía o líder
}
