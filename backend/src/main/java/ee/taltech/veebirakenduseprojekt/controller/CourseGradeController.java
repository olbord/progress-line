package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}")
public class CourseGradeController {

    private final CourseService courseService;

    @GetMapping("/students/{studentId}/grades")
    @ResponseStatus(HttpStatus.OK)
    public List<GradeItemDto> getGradeItemsByStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return courseService.getAllStudentGradesByStudentId(courseId, studentId);
    }

    @GetMapping("/progress-lines")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentProgressLineDto> getCourseProgressLineData(@PathVariable Long courseId) {
        return courseService.getCourseProgressLineData(courseId);
    }
}
