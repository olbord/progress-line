package ee.taltech.veebirakenduseprojekt.controller;

import lombok.Data;
import java.time.*;

@Data
public class CommentResponseDto {
    private Long id;
    private LocalDateTime dateTime;
    private CommentType type;
    private String commentText;
    private Long teacherId;
    private StudentDto student;
}
