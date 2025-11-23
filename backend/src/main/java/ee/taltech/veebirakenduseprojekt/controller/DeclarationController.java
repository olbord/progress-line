package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.DeclarationService;
import ee.taltech.veebirakenduseprojekt.service.CourseAccessService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Declaration Controller",
        description = "Endpoints to manage students declarations to a specific course.")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/courses/{courseId}/declarations")
public class DeclarationController {

    private final DeclarationService declarationService;
    private final CourseAccessService accessService;

    @Operation(summary = "Delete declaration",
            description = "Removes the declaration to the course if student has changed their study plan.")
    @DeleteMapping("/student/{studentId}/no-declaration")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentDeclaration(@PathVariable Long courseId,
                                         @PathVariable Long studentId,
                                         @AuthenticationPrincipal(expression = "email") String email) {
        accessService.requireTeacherAccess(email, courseId);
        declarationService.deleteStudentDeclarationById(studentId, courseId);
    }

    @Operation(summary = "Add declaration", description = "Adds student to the declaration list for a course.")
    @PostMapping("/student/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addStudentDeclaration(@PathVariable Long courseId,
                                      @PathVariable Long studentId,
                                      @AuthenticationPrincipal(expression = "email") String email) {
        accessService.requireTeacherAccess(email, courseId);
        declarationService.addStudentToDeclaration(courseId, studentId);
    }
}
