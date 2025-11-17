package ee.taltech.veebirakenduseprojekt.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "feedback_response")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private FeedbackAttempt attempt;

    private String name;
    private String printval;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
