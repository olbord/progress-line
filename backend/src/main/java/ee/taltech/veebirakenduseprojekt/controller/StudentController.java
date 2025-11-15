package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Student Controller", description = "Endpoints to manage general student data.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/students")
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Retrieve student personal data",
            description = "Gets student name and email by ID.")
    @GetMapping("/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public StudentPersonalDataResponseDto getStudentPersonalDataById(
            @PathVariable Long studentId) {
        return studentService.getStudentPersonalDataById(studentId);
    }
}
