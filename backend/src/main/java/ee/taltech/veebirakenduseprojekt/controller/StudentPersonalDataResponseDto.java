package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@Schema(name = "Student Personal Data DTO",
        description = "Students personal data: name and email, response to request by ID")
public class StudentPersonalDataResponseDto {
    @Schema(description = "The unique ID of the student",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "The students first name", example = "Diana")
    private String firstName;

    @Schema(description = "The students last name", example = "Lee")
    private String lastName;

    @Schema(description = "The students email address", example = "diale@taltech.ee")
    private String email;
}
