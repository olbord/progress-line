package ee.taltech.veebirakenduseprojekt.repository;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inactive")
@Getter
@Setter
public class Inactive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    // TODO: inactive table should have a column "course_id"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Student student;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
}
