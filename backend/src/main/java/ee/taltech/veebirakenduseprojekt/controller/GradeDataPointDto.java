package ee.taltech.veebirakenduseprojekt.controller;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GradeDataPointDto {
    private LocalDateTime x;
    private BigDecimal y;
}