package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.service.*;
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

@Tag(name = "Course Student Controller", description = "Endpoints to manage students on a specific course.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/students")
public class CourseStudentController {

    private final CourseService courseService;
    private final CourseAccessService accessService;
    private final StudentService studentService;

    @Operation(summary = "Get a specific student's personal data on a course",
            description = "Gets a single student's name and email by their ID, but only if they are on the specified course.")
    @GetMapping("/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public StudentPersonalDataResponseDto getStudentOnCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @AuthenticationPrincipal(expression = "email") String email) {
        accessService.requireTeacherAccess(email, courseId);
        return studentService.getStudentPersonalDataById(studentId);
    }

    @Operation(summary = "Get paginated list of active students with declarations",
            description = "Gets page of students who have declared participation on a specific course and are not inactive.")
    @GetMapping("/with-declarations")
    @ResponseStatus(HttpStatus.OK)
    public Page<StudentDto> getActiveStudentsWithDeclarations(
            @PathVariable Long courseId,
            @AuthenticationPrincipal(expression = "email") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        accessService.requireTeacherAccess(email, courseId);
        Pageable pageable = PageRequest.of(page, size);
        return courseService.getPaginatedActiveStudentsWithDeclarations(courseId, pageable);
    }

    @Operation(summary = "Get paginated list of students without declaration",
            description = "Gets page of students who have not declared participation on a specific course.")
    @GetMapping("/no-declarations")
    @ResponseStatus(HttpStatus.OK)
    public Page<StudentDto> getStudentsWithoutDeclarations(
            @PathVariable Long courseId,
            @AuthenticationPrincipal(expression = "email") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        accessService.requireTeacherAccess(email, courseId);
        Pageable pageable = PageRequest.of(page, size);
        return courseService.getPaginatedStudentsWithoutDeclarations(courseId, pageable);
    }

    @Operation(summary = "Get paginated list of inactive students",
            description = "Gets page of students who are marked as inactive on a specific course.")
    @GetMapping("/inactive")
    @ResponseStatus(HttpStatus.OK)
    public Page<StudentDto> getInactiveStudents(
            @PathVariable Long courseId,
            @AuthenticationPrincipal(expression = "email") String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        accessService.requireTeacherAccess(email, courseId);
        Pageable pageable = PageRequest.of(page, size);
        return courseService.getPaginatedInactiveStudents(courseId, pageable);
    }

    @Operation(summary = "Mark a student as inactive",
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

    @Operation(summary = "Delete student inactivity label",
            description = "Removes the student inactivity label if student has changed his/her activity.")
    @DeleteMapping("/{studentId}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markActive(@PathVariable Long courseId,
                                         @PathVariable Long studentId,
                                         @AuthenticationPrincipal(expression = "email") String email) {
        accessService.requireTeacherAccess(email, courseId);
        courseService.markStudentActive(courseId, studentId);
    }
}
