package ee.taltech.veebirakenduseprojekt.entity;

import lombok.*;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "email")
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "body", columnDefinition = "TEXT", nullable = false)
    private String body;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "sent_at", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE", nullable = false)
    private LocalDateTime sentAt;
}
