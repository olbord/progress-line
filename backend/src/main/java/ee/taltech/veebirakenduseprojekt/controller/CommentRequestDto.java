package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
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
    private Long studentId;

    @Schema(description = "Type of comment as specified by CommentType enum")
    private CommentType type;

    @Schema(description = "Text describing students progress",
            example = "Has not participated since exercise 2.")
    private String commentText;

    @Schema(description = "The unique ID of the user (student)",
            example = "11")
    private Long teacherId;

    @Schema(description = "The unique ID of the course",
            example = "35065")
    private Long courseId;
}
