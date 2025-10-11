package ee.taltech.veebirakenduseprojekt.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import ee.taltech.veebirakenduseprojekt.service.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getAllComments() {
        return commentService.getAllComments();
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto getCommentById(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    @GetMapping("/students/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getCommentsByStudent(@PathVariable Long studentId) {
        return commentService.getCommentsByStudentId(studentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@RequestBody CommentRequestDto dto) {
        return commentService.addComment(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto dto) {
        return commentService.updateComment(id, dto);
    }

}

