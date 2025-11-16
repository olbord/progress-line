package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Student Feedback Controller",
        description = "Endpoints to retrieve student feedback responses.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/students/{studentId}/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final CourseAccessService accessService;

    @Operation(summary = "Retrieve a student's feedback responses",
            description = "Gets all feedback responses submitted by a specific student for a course.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FeedbackResponseDto> getStudentFeedback(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @AuthenticationPrincipal(expression = "email") String email) {

        accessService.requireTeacherAccess(email, courseId);

        return feedbackService.getStudentFeedback(courseId, studentId);
    }
}
