package ee.taltech.veebirakenduseprojekt.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Grade Data Point DTO",
        description = "Axis data for cumulative grades sum graph")
public class GradeDataPointDto {
    @Schema(description = "Time of data point",
            example = "2025-09-24T15:00:00")
    @DateTimeFormat(pattern="yyyy-mm-ddHH:mm:ss")
    private LocalDateTime x;

    @Schema(description = "Cumulative sum of grades",
            example = "250")
    @NotNull(message="Sum of grades cannot be null")
    @Min(value = 0, message = "Sum of grades cannot be negative")
    private BigDecimal y;
}
