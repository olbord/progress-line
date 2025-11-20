package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.*;

@Data
@Schema(name = "Student Progress Line DTO",
        description = "List of all grade data points of a students to generate graoh")
public class StudentProgressLineDto {
    @Schema(description = "The unique ID of the student",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Student ID cannot be null")
    private Long studentId;

    @Schema(description = "The students first name", example = "Diana")
    @NotNull(message = "Student first name cannot be null")
    private String firstName;

    @Schema(description = "The students last name", example = "Lee")
    @NotNull(message = "Student last name cannot be null")
    private String lastName;

    @Schema(description = "List of Grade Data Point DTO-s to generate graph")
    private List<GradeDataPointDto> dataPoints;
}
