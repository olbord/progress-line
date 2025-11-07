package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.Data;

@Data
@Schema(name = "Course DTO",
        description = "Basic name and ID data of a course")
public class CourseDto {

    @Schema(description = "The unique ID of the course",
            example = "35065", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "The unique short name of the course: code with year",
            example = "ITI0102-2025")
    private String shortName;

    @Schema(description = "The unique full name of the course",
            example = "ITI0102 Programmeerimise algkursus 2025/2026")
    private String fullName;
}
