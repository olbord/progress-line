package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.Data;

@Data
@Schema(name = "Course Request DTO",
        description = "Query to request course data")
public class CourseRequestDto {
    @Schema(description = "Optional query")
    private String query;
}
