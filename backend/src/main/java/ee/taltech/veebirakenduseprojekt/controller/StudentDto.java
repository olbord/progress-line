package ee.taltech.veebirakenduseprojekt.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Data
public class StudentDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<GradeItemDto> grades;

    @JsonProperty("totalPoints")
    public BigDecimal getTotalPoints() {
        if (grades == null || grades.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return grades.stream()
                .map(GradeItemDto::getGrade)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
