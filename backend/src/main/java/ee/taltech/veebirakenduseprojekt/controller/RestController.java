package ee.taltech.veebirakenduseprojekt.controller;

import lombok.*;
import org.springframework.web.bind.annotation.*;

import java.time.*;

@org.springframework.web.bind.annotation.RestController
//Placeholder for @RequiredArgsConstructor
public class RestController {

    //placeholder for bean private final gradeService gradeService;

    public RestController() {
        //this.gradeServicePlaceholder = gradeServicePlaceholder;
    }

    //Client uses JSON to POST new comment
    @PostMapping()
    // Placeholder for response status
    public String postComment(@RequestBody CommentDto comment) {
        return comment.getComment();
    }
    //Placeholder for @GetMapping, @DeletaMapping, @UpdateMapping...

}
