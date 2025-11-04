package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseAccessService;
import ee.taltech.veebirakenduseprojekt.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/grades")
public class CourseGradeController {

    private final CourseService courseService;
    private final CourseAccessService accessService;

    @GetMapping("/students/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GradeItemDto> getGradeItemsByStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);
        return courseService.getAllStudentGradesByStudentId(courseId, studentId);
    }

    @GetMapping("/progress-lines")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentProgressLineDto> getCourseProgressLineData(
            @PathVariable Long courseId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);
        return courseService.getCourseProgressLineData(courseId);
    }
}
