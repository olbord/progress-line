package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Course Grade Controller",
        description = "Endpoints to manage students grades.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}")
public class CourseGradeController {

    private final CourseService courseService;
    private final GradeItemService gradeItemService;
    private final CourseAccessService accessService;

    @Operation(summary = "Require all students grades",
            description = "Gets all comments grades on a specific course.")
    @GetMapping("/students/{studentId}/grades")
    @ResponseStatus(HttpStatus.OK)
    public List<GradeItemDto> getGradeItemsByStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);
        return courseService.getAllStudentGradesByStudentId(courseId, studentId);
    }

    @Operation(summary = "Require students cumulative sum of grades",
            description = "Gets all students grades as data points to create a graph of cumulative sum of grades.")
    @GetMapping("/progress-lines")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentProgressLineDto> getCourseProgressLineData(
            @PathVariable Long courseId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);
        return courseService.getCourseProgressLineData(courseId);
    }

    @Operation(summary = "Get the latest grade for a student on a course",
            description = "Returns the most recent grade item.")
    @GetMapping("/students/{studentId}/latest-grade")
    @ResponseStatus(HttpStatus.OK)
    public LastGradeDto getLastGrade(@PathVariable Long courseId,
                                     @PathVariable Long studentId,
                                     @AuthenticationPrincipal(expression = "email") String email) {
        accessService.requireTeacherAccess(email, courseId);

        return gradeItemService.getLatestGrade(courseId, studentId);
    }
}
