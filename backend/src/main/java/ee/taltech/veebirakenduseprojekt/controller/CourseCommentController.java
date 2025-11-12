package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Course Comment Controller", description = "Endpoints to manage comments describing students progress.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/students/{studentId}/comments")
public class CourseCommentController {

    private final CourseService courseService;
    private final CourseAccessService accessService;
    private final CommentService commentService;

    @Operation(summary = "Retrieve all students comments",
            description = "Gets all comments comment describing students progress on a specific course.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentsByStudent(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);
        return courseService.getCommentsByStudentId(courseId, studentId);
    }

    @Operation(summary = "Add a new comment",
            description = "Adds a new comment describing students progress written by a teacher to the database.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@RequestBody CommentRequestDto dto) {
        return commentService.addComment(dto);
    }

    // TODO ADD TO FRONTEND OR DELETE
    @Operation(summary = "[NOT USED] Update a comment",
            description = "Changes the comment describing a students progress.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto dto) {
        return commentService.updateComment(id, dto);
    }

}
