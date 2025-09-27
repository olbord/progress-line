package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.StudentService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/no_declarations")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getStudentsWithoutDeclarations() {
        return studentService.getAllStudentsWithoutDeclarations();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentDto getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }
}
