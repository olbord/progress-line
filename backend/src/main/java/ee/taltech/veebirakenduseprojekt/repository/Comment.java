package ee.taltech.veebirakenduseprojekt.repository;

import ee.taltech.veebirakenduseprojekt.controller.*;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "\"comment\"")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTime;
    @Enumerated(EnumType.STRING)
    private CommentType type;
    @Column(name = "\"comment\"")
    private String commentText;

    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "user_id", nullable = false)
    private Long teacherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Student student;
}
