package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.exception.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.*;

@org.springframework.web.bind.annotation.RestController
//Placeholder for @RequiredArgsConstructor
public class RestController {

    //placeholder for bean private final ... service

    public RestController(CommentRepository commentRepository) {
    }

    //Client uses JSON to POST new comment
    @PostMapping()
    // Placeholder for response status
    public String postComment(@RequestBody CommentDto comment) {
        return comment.getComment();
    }

    //Placeholder for @GetMapping, @DeletaMapping, @UpdateMapping...

}
