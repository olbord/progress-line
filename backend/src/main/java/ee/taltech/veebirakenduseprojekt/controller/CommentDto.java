package ee.taltech.veebirakenduseprojekt.controller;

import lombok.*;

import java.time.*;

@Data
public class CommentDto {
    private Long id;
    private Long userId;
    private LocalDateTime dateTime;
    private CommentType type;
    private String comment;
    private Long teacherId;
}
