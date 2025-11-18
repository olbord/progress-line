package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(name = "Teacher DTO", description = "Teachers name and email")
public class TeacherDto {

    @Schema(description = "The teachers first name", example = "Diana")
    @NotNull(message = "Teacher first name cannot be null")
    private String firstName;

    @Schema(description = "The teachers last name", example = "Lee")
    @NotNull(message = "Teacher last name cannot be null")
    private String lastName;

    @Schema(description = "The teachers email address", example = "diale@taltech.ee")
    @NotNull(message = "Student email cannot be null")
    @Email(message = "Email should be valid")
    private String email;
}
