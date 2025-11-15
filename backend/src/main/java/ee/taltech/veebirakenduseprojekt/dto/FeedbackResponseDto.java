package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;
@Data
@Schema(name = "Student Feedback Response DTO",
        description = "Contains the details of a single student's answer to a feedback question.")
public class FeedbackResponseDto {

    @Schema(description = "The ID of the feedback response entry.", example = "1")
    private Long responseId;

    @Schema(description = "The text of the feedback question.", example = "Tagasiside praktikumidele")
    private String questionName;

    @Schema(description = "The value of the student's answer.", example = "Jah")
    private String printValue;
}
