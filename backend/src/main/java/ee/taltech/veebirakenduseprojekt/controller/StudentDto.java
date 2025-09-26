package ee.taltech.veebirakenduseprojekt.controller;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StudentDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private List<LocalDateTime> commentDateTimes;
}
