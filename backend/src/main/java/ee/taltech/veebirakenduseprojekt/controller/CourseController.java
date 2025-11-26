package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.service.CourseService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@Tag(name = "Course Controller", description = "Endpoints to manage courses.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses")
public class CourseController {

    private final CourseService courseService;

    @Operation(summary = "Require all courses",
            description = "Gets all courses that the logged in user is authenticated to see.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CourseDto> getCourses(
            @AuthenticationPrincipal(expression = "email") String email,
            @ModelAttribute CourseRequestDto searchRequest
    ) {
        return courseService.getCourses(email, searchRequest);
    }
}
