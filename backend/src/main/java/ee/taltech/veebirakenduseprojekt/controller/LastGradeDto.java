package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import java.time.*;

@Data
@Schema(name = "Last Grade DTO",
        description = "Data of the last grade of a student for email draft")
public class LastGradeDto {
    @Schema(description = "The unique name of the grade item" +
            "All grades of the same exercise have the same item name",
            example = "EX/ex00_hello - Tests")
    private String itemName;

    @Schema(description = "The numeric (points) value of a students grade",
            example = "5")
    private String grade;

    @Schema(description = "The date and time when the grade was added",
            example = "2025-09-24T15:00:00")
    private LocalDateTime gradeDateGraded;
}
