package ee.taltech.veebirakenduseprojekt.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "enrolled_user")
@Getter
@Setter
public class MoodleEnrollment {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}
