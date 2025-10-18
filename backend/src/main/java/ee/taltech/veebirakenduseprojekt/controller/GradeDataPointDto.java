package ee.taltech.veebirakenduseprojekt.controller;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GradeDataPointDto {
    private LocalDateTime x;
    private BigDecimal y;
}