package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Email Controller",
        description = "Endpoints to send email to student.")
@RestController
@RequiredArgsConstructor
@ResponseStatus(HttpStatus.OK)
@RequestMapping("api/courses/{courseId}/students/{studentId}")
public class EmailController {

    private final CourseService courseService;
    private final CourseAccessService accessService;

    @Operation(summary = "Send and save email",
            description = "Sends email with specific body to student and saves to database.")
    @PostMapping("/send-email")
    @ResponseStatus(HttpStatus.OK)
    public void sendGradeEmail(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @Valid @RequestBody EmailRequestDto dto,
            @AuthenticationPrincipal(expression = "email") String senderEmail) {

        accessService.requireTeacherAccess(senderEmail, courseId);

        courseService.sendAndSaveEmail(courseId, studentId, dto.getEmailBody(), senderEmail);
    }
}
