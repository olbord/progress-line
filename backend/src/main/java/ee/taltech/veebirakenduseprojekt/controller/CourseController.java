package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseService;
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

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDto> getCourses(@RequestBody(required = false) CourseSearchRequest searchRequest) {
        // TODO: replace teacherId with app user's id from the UserDetails - security
        return courseService.getCourses(3L, searchRequest);
    }
}
