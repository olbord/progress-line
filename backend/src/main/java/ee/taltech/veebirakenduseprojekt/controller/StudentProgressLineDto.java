package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import java.util.*;

@Data
@Schema(name = "Student Progress Line DTO",
        description = "List of all grade data points of a students to generate graoh")
public class StudentProgressLineDto {
    @Schema(description = "The unique ID of the student",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    private Long studentId;

    @Schema(description = "The students first name", example = "Diana")
    private String firstName;

    @Schema(description = "The students last name", example = "Lee")
    private String lastName;

    @Schema(description = "List of Grade Data Point DTO-s to generate graph")
    private List<GradeDataPointDto> dataPoints;
}
