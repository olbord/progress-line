package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseAccessService;
import ee.taltech.veebirakenduseprojekt.service.CourseService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Tag(name = "Course Student Controller", description = "Endpoints to manage students on a specific course.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/students")
public class CourseStudentController {

    private final CourseService courseService;
    private final CourseAccessService accessService;

    @Operation(summary = "Get paginated list of active students with declarations")
    @GetMapping("/with-declarations")
    @ResponseStatus(HttpStatus.OK)
    public Page<StudentDto> getActiveStudentsWithDeclarations(
            @PathVariable Long courseId,
            @AuthenticationPrincipal(expression = "email") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        accessService.requireTeacherAccess(email, courseId);
        Pageable pageable = PageRequest.of(page, size);
        return courseService.getAllActiveStudentsWithDeclarations(courseId, pageable);
    }

    @Operation(summary = "Require all students without declaration",
            description = "Gets all students who have not declared participation on a specific course.")
    @GetMapping("/no-declarations")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getStudentsWithoutDeclarations(
            @PathVariable Long courseId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);
        return courseService.getAllStudentsWithoutDeclarations(courseId);
    }

    @Operation(summary = "Require all inactive students",
            description = "Gets all students who are marked as inactive on a specific course.")
    @GetMapping("/inactive")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentDto> getInactiveStudents(
            @PathVariable Long courseId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);
        return courseService.getAllInactiveStudents(courseId);
    }

    // TODO: ADD TO FRONTEND
    @Operation(summary = "[ADD TO FRONTEND] Mark a student as inactive",
            description = "Marks a student as inactive on a specific course.")
    @PostMapping("/{studentId}/inactive")
    public ResponseEntity<String> markInactive(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);

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
