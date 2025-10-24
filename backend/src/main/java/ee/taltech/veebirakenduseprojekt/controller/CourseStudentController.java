package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/students")
public class CourseStudentController {

    private final CourseService courseService;

    @GetMapping("/with-declarations")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getActiveStudentsWithDeclarations(@PathVariable Long courseId) {
        return courseService.getAllActiveStudentsWithDeclarations(courseId);
    }

    @GetMapping("/no-declarations")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getStudentsWithoutDeclarations(@PathVariable Long courseId) {
        return courseService.getAllStudentsWithoutDeclarations(courseId);
    }

    @GetMapping("/inactive")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getInactiveStudents(@PathVariable Long courseId) {
        return courseService.getAllInactiveStudents(courseId);
    }

    @PostMapping("/{studentId}/inactive")
    public ResponseEntity<String> markInactive(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        try {
            courseService.markStudentInactive(courseId, studentId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Student marked as inactive successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
