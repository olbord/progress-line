package ee.taltech.veebirakenduseprojekt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

public class FeedbackItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;

    // The question asked from student
    private String name;
    private String label;

    // Type of question: multiple choice, textarea
    private String typ;
    private Long position;
    private LocalDateTime createdAt;
}
