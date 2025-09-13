package ee.taltech.veebirakenduseprojekt.controller;

import lombok.*;

import java.math.BigDecimal;
import java.time.*;

@Data
public class GradeItemDto {
    private Long id;
    private Long courseId;
    private Long userId;
    private String itemName;
    private String itemType;
    private BigDecimal grade;
    private LocalDateTime gradeDateGraded;
}
