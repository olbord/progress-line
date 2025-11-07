package ee.taltech.veebirakenduseprojekt.controller;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import ee.taltech.veebirakenduseprojekt.service.*;

@Tag(name = "Comment management", description = "Endpoints to manage comments describing students progress.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;

    // TODO: delete this method if not used
    @Operation(summary = "Get all comments",
            description = "Gets all comments ever written.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getAllComments() {
        return commentService.getAllComments();
    }

    // TODO: delete this method if not used
    @Operation(summary = "Get comment by ID",
            description = "Get a comment by its ID.")
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto getCommentById(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    @Operation(summary = "Add a comment",
            description = "Adds a comment describing students progress written by a teacher to the database.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@RequestBody CommentRequestDto dto) {
        return commentService.addComment(dto);
    }

    @Operation(summary = "Update a comment",
            description = "Changes the comment describing a students progress.")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto dto) {
        return commentService.updateComment(id, dto);
    }
}

