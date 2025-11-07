package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

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
    private LocalDateTime x;

    @Schema(description = "Cumulative sum of grades",
            example = "250")
    private BigDecimal y;
}
