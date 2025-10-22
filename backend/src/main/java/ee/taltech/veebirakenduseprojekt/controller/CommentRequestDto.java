package ee.taltech.veebirakenduseprojekt.controller;

import lombok.Data;

@Data
public class CommentRequestDto {
    private Long studentId;
    private CommentType type;
    private String commentText;
    private Long teacherId;
    private Long courseId;
}
