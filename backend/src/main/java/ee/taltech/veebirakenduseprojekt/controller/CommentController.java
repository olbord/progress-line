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

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto getCommentById(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    @GetMapping("/student/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDto> getCommentsByUser(@PathVariable Long userId) {
        return commentService.getCommentsByUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@RequestBody CommentDto dto) {
        return commentService.addComment(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto updateComment(@PathVariable Long id, @RequestBody CommentDto dto) {
        return commentService.updateComment(id, dto);
    }

}

