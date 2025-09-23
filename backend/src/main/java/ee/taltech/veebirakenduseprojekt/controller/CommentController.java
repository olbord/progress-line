package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.repository.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
@RestController
public class CommentController {

    private final CommentRepository commentRepository;

    public CommentController(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        //this.commentService = commentService
    }

    @GetMapping("comments/{id}")
    public Comment getComments(@PathVariable Long id) {
        if(id < 0) {
            //throw new ApplicationException("Kasutaja id peab olema positiivne arv");
        }
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.get();
    }

}

