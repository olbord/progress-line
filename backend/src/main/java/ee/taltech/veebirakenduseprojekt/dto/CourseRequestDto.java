package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(name = "Course Request DTO",
        description = "Query to request course data")
public class CourseRequestDto {
    @Schema(description = "Optional query")
    private String query;
}
