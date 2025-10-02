package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.InactiveService;
import ee.taltech.veebirakenduseprojekt.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
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
    private final InactiveService inactiveService;

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

    @PostMapping("/{id}/inactive")
    public ResponseEntity<String> markInactive(@PathVariable Long id) {
        try {
            inactiveService.addInactive(id);
            return ResponseEntity.ok("Student marked as inactive successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
