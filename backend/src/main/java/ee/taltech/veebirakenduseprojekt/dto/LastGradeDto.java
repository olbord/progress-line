package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.*;

import java.time.*;

@Data
@Schema(name = "Last Grade DTO",
        description = "Data of the last grade of a student for email draft")
public class LastGradeDto {
    @Schema(description = "The unique name of the grade item" +
            "All grades of the same exercise have the same item name",
            example = "EX/ex00_hello - Tests")
    @NotNull(message = "Grade name cannot be null")
    private String itemName;

    @Schema(description = "The numeric (points) value of a students grade",
            example = "5")
    private String grade;

    @Schema(description = "The date and time when the grade was added",
            example = "2025-09-24T15:00:00")
    @DateTimeFormat(pattern="yyyy-mm-ddHH:mm:ss")
    private LocalDateTime gradeDateGraded;
}
