package ee.taltech.veebirakenduseprojekt.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "grade_item")
public class GradeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long courseId;
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    private String itemName;
    private String itemType;
    private BigDecimal grade;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Student student;
}
