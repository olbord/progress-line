package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.Data;

@Data
@Schema(name = "Teacher DTO", description = "Teachers name and email")
public class TeacherDto {

    @Schema(description = "The teachers first name", example = "Diana")
    private String firstName;

    @Schema(description = "The teachers last name", example = "Lee")
    private String lastName;

    @Schema(description = "The teachers email address", example = "diale@taltech.ee")
    private String email;
}
