package ee.taltech.veebirakenduseprojekt.repository;

import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "inactive")
@Setter
public class Inactive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Student student;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;
}
