package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Schema(name = "Email Request DTO",
        description = "Email body created in frontend with student and course ID")
public class EmailRequestDto {

    @Schema(description = "The email body written by teacher",
            example = "Tere! Kuidas sul läheb? Abiõppejõud")
    @NotNull(message = "Email body cannot be null")
    private String emailBody;

}
