package ee.taltech.veebirakenduseprojekt.repository;

import jakarta.persistence.*;
import lombok.*;

import java.time.*;
@Data
@Entity
@Table(name="comment")
public class Comment {
    @Id
    private Long id;
    @Column(name="user_id")
    private Long user_id;
    private LocalDateTime date_time;
    private String Type;
    private String comment;
    //@ManyToOne(fetch = FetchType.LAZY)
    private Long teacherId;

}
