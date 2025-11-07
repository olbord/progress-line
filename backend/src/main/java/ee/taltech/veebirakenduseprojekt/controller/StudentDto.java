package ee.taltech.veebirakenduseprojekt.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Data
@Schema(name = "Student DTO",
        description = "Students personal and grade data")
public class StudentDto {
    @Schema(description = "The unique ID of the student",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "The students first name", example = "Diana")
    private String firstName;

    @Schema(description = "The students last name", example = "Lee")
    private String lastName;

    @Schema(description = "The students email address", example = "diale@taltech.ee")
    private String email;

    @Schema(description = "The list of students grades")
    private List<GradeItemDto> grades;

    @Schema(description = "The number of students total grade points")
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
