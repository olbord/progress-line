package ee.taltech.veebirakenduseprojekt.controller;

import lombok.Data;

@Data
public class CourseDto {
    private Long id;
    private String shortName;
    private String fullName;
}
