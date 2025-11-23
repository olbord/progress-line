package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Schema(name = "Student status DTO",
        description = "Students status: declaration and activity")
@Builder
@AllArgsConstructor
public class StudentStatusDto {

    @Schema(description = "The unique ID of the student",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @Schema(description = "The unique ID of the course",
            example = "35065", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    @Schema(description = "The unique ID of the course",
            example = "false", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Status cannot be null")
    private boolean active;

    @Schema(description = "The unique ID of the course",
            example = "false", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Status cannot be null")
    private boolean declared;
}
