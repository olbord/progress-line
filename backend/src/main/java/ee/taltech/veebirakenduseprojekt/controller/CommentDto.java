package ee.taltech.veebirakenduseprojekt.controller;

import lombok.Data;

import java.time.*;

@Data
public class CommentDto {
    private Long id;
    private Long userId;
    private LocalDateTime dateTime;
    private CommentType type;
    private String commentText;
    private Long teacherId;
}
