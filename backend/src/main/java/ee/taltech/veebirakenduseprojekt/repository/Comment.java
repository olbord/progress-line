package ee.taltech.veebirakenduseprojekt.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.*;
@Entity
@Getter @Setter
@Table(name="comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_id")
    private Long userId;
    private LocalDateTime dateTime;
    private String type;
    private String comment;
    //@ManyToOne(fetch = FetchType.LAZY)
    private Long teacherId;

}
