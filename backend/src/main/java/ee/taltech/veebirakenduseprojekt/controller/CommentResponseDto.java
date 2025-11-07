package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.Data;
import java.time.*;

@Data
@Schema(name = "Comment Response DTO",
        description = "Comment data sent as a response to get request")
public class CommentResponseDto {
    @Schema(description = "The unique ID of the comment",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "The date when the comment was added",
            example = "2025-09-24T15:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dateTime;

    @Schema(description = "Type of comment as specified by CommentType enum")
    private CommentType type;

    @Schema(description = "Text describing students progress",
            example = "Has not participated since exercise 2.")
    private String commentText;

    @Schema(description = "The unique ID of teacher",
            example = "11")
    private Long teacherId;

    @Schema(description = "The unique ID of the student",
            example = "11")
    private StudentDto student;

    @Schema(description = "The unique ID of the course",
            example = "35065")
    private CourseDto course;
}
