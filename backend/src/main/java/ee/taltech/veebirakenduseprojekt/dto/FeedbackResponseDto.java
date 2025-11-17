package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import java.time.*;

@Data
@Schema(name = "Student Feedback Response DTO",
        description = "Contains the details of a single student's answer to a feedback question.")
public class FeedbackResponseDto {

    @Schema(description = "The name of the questionnaire.", example = "Tagasiside 1. nädal")
    private String feedbackName;

    @Schema(description = "The ID of the feedback response entry.", example = "1")
    private Long responseId;

    @Schema(description = "The text of the feedback question.", example = "Milline on nesetunne seoses ainega?")
    private String questionName;

    @Schema(description = "The value of the student's answer.", example = "Jah")
    private String printValue;

    @Schema(description = "The time when the response was created.", example = "2025-11-16T07:54:21")
    private LocalDateTime responseTime;
}
