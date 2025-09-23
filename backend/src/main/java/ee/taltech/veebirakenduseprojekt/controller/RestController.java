package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.repository.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
//Placeholder for @RequiredArgsConstructor
public class RestController {

    //placeholder for bean private final gradeService gradeService;
    private final CommentRepository commentRepository;

    public RestController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        //this.commentService = commentService
        //this.gradeServicePlaceholder = gradeServicePlaceholder;
    }

    //Client uses JSON to POST new comment
    @PostMapping()
    // Placeholder for response status
    public String postComment(@RequestBody CommentDto comment) {
        return comment.getComment();
    }

    @GetMapping("comments/{userId}")
    public Comment getComments(@PathVariable Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.get();
    }
    //Placeholder for @GetMapping, @DeletaMapping, @UpdateMapping...

}
