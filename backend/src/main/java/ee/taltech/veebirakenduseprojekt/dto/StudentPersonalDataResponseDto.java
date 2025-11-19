package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Schema(name = "Student Personal Data DTO",
        description = "Students personal data: name and email, response to request by ID")
public class StudentPersonalDataResponseDto {
    @Schema(description = "The unique ID of the student",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "Student ID cannot be null")
    private Long id;

    @Schema(description = "The students first name", example = "Diana")
    @NotNull(message = "Student first name cannot be null")
    private String firstName;

    @Schema(description = "The students last name", example = "Lee")
    @NotNull(message = "Student last name cannot be null")
    private String lastName;

    @Schema(description = "The students email address", example = "diale@taltech.ee")
    @NotNull(message = "Student email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
}
