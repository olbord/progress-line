package ee.taltech.veebirakenduseprojekt.dto;

import ee.taltech.veebirakenduseprojekt.entity.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(name = "Comment Request DTO",
        description = "New comment data without date")
public class CommentRequestDto {
    @Schema(description = "The unique ID of the user (student)",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @Schema(description = "Type of comment as specified by CommentType enum")
    @NotNull(message = "Comment type cannot be null")
    private CommentType type;

    @Schema(description = "Text describing students progress",
            example = "Has not participated since exercise 2.")
    @NotNull(message = "Comment text cannot be null")
    private String commentText;

    @Schema(description = "The unique ID of the user (student)",
            example = "11")
    @NotNull(message = "Teachers ID cannot be null")
    private Long teacherId;

    @Schema(description = "The unique ID of the course",
            example = "35065")
    @NotNull(message = "Course cannot be null")
    private Long courseId;
}
