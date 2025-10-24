package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/students/{studentId}/comments")
public class CourseCommentController {

    private final CourseService courseService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentsByStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId) {
        return courseService.getCommentsByStudentId(courseId, studentId);
    }
}
