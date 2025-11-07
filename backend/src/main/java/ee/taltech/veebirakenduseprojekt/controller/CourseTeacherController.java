package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name = "Course Teacher Controller",
        description = "Endpoints to manage teachers on a specific course.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/teachers")
public class CourseTeacherController {

    private final CourseService courseService;

    // TODO. ADD TO FRONTEND
    @Operation(summary = "[ADD TO FRONTEND] Require all teachers on a specific course",
            description = "Gets all teachers on a specific course.")
    @GetMapping
    public List<TeacherDto> getTeachersByCourse(@PathVariable Long courseId) {
        return courseService.getTeachers(courseId);
    }
}
