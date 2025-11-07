package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.*;

@Data
@Schema(name = "Grade Item DTO",
        description = "Data of a single grade of a student")
public class GradeItemDto {
    @Schema(description = "The unique ID of the grade item." +
            "All grades of the same exercise have the same ID",
            example = "159069", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "The unique ID of the course",
            example = "35065")
    private Long courseId;

    @Schema(description = "The unique ID of the student",
            example = "11")
    private Long userId;

    @Schema(description = "The unique name of the grade item" +
            "All grades of the same exercise have the same item name",
            example = "EX/ex00_hello - Tests")
    private String itemName;

    @Schema(description = "Type of the grade item",
            example = "mod")
    private String itemType;

    @Schema(description = "The numeric (points) value of a students grade",
            example = "5")
    private BigDecimal grade;

    @Schema(description = "The date when the grade was added",
            example = "2025-09-24T15:00:00")
    private LocalDateTime gradeDateGraded;
}
