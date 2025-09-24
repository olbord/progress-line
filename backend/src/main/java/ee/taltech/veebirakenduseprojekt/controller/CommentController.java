package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.repository.*;
import ee.taltech.veebirakenduseprojekt.service.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/api/comments/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public CommentDto getCommentById(@PathVariable Long id) {
        return commentService.getComment(id);
        //new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment was not found")
    }


    //Client uses JSON to POST new comment
    //@PostMapping()
    //@ResponseStatus(Http.Status.CREATED)
    // Placeholder for response status
    //public String postComment(@RequestBody CommentDto comment) {
    //return comment.getComment();
    //}

}

