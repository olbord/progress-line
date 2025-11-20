package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.*;

import java.math.BigDecimal;
import java.time.*;

@Data
@Schema(name = "Grade Item DTO",
        description = "Data of a single grade of a student")
public class GradeItemDto {
    @Schema(description = "The unique ID of the grade item." +
            "All grades of the same exercise have the same ID",
            example = "159069", accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull(message = "ID cannot be null")
    private Long id;

    @Schema(description = "The unique ID of the course",
            example = "35065")
    @NotNull(message = "Course ID cannot be null")
    private Long courseId;

    @Schema(description = "The unique ID of the student",
            example = "11")
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @Schema(description = "The unique name of the grade item" +
            "All grades of the same exercise have the same item name",
            example = "EX/ex00_hello - Tests")
    @NotNull(message = "Grade name cannot be null")
    private String itemName;

    @Schema(description = "Type of the grade item",
            example = "mod")
    private String itemType;

    @Schema(description = "The numeric (points) value of a students grade",
            example = "5")
    private BigDecimal grade;

    @Schema(description = "The date and time when the grade was added",
            example = "2025-09-24T15:00:00")
    @DateTimeFormat(pattern="yyyy-mm-ddHH:mm:ss")
    private LocalDateTime gradeDateGraded;
}
