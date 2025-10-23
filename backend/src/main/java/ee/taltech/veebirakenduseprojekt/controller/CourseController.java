package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses")
public class CourseController {

    private final CourseService courseService;


    // --- course endpoints ---

    @PostMapping
    public List<CourseDto> getCourses(@RequestBody(required = false) CourseSearchRequest searchRequest) {
        return courseService.getCourses(searchRequest);
    }


    // --- student endpoints ---

    @GetMapping("/{courseId}/students/with-declarations")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getActiveStudentsWithDeclarations(@PathVariable Long courseId) {
        return courseService.getAllActiveStudentsWithDeclarations(courseId);
    }

    @GetMapping("/{courseId}/students/no-declarations")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getStudentsWithoutDeclarations(@PathVariable Long courseId) {
        return courseService.getAllStudentsWithoutDeclarations(courseId);
    }

    @GetMapping("/{courseId}/students/inactive")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getInactiveStudents(@PathVariable Long courseId) {
        return courseService.getAllInactiveStudents(courseId);
    }

    @PostMapping("/{courseId}/students/{studentId}/inactive")
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


    // --- comment endpoints ---

    @GetMapping("/{courseId}/students/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentsByStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        return courseService.getCommentsByStudentId(courseId, studentId);
    }
}
