package ee.taltech.veebirakenduseprojekt.controller;

import lombok.*;

import java.time.*;

@Data
public class GradeItemDto {
    private Long id;
    private Long courseId;
    private Long userId;
    private String itemName;
    private String itemType;
    private Double grade;
    private LocalDateTime gradeDateGraded;
}
