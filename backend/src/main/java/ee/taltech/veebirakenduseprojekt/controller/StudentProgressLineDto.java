package ee.taltech.veebirakenduseprojekt.controller;

import lombok.*;

import java.util.*;

@Data
public class StudentProgressLineDto {
    private Long studentId;
    private String firstName;
    private String lastName;
    private List<GradeDataPointDto> dataPoints;
}
