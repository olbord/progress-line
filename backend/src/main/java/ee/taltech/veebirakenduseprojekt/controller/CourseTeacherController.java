package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/teachers")
public class CourseTeacherController {

    private final CourseService courseService;

    @GetMapping
    public List<TeacherDto> getTeachersByCourse(@PathVariable Long courseId) {
        return courseService.getTeachers(courseId);
    }
}
