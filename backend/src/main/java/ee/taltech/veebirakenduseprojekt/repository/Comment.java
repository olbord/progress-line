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
    private Long user_id;
    private LocalDateTime date_time;
    private String Type;
    private String comment;
    private Long teacherId;

}
