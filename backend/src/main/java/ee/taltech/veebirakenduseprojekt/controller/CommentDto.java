package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.repository.*;
import lombok.Data;
import java.time.*;

@Data
public class CommentDto {
    private Long id;
    private LocalDateTime dateTime;
    private CommentType type;
    private String commentText;
    private Long teacherId;
    private StudentDto student;
}
