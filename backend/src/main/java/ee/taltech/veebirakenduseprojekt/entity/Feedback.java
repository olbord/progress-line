package ee.taltech.veebirakenduseprojekt.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "feedback")
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
