package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.DeclarationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/declarations")
public class DeclarationController {

    private final DeclarationService declarationService;

    @DeleteMapping("/student/{studentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudentDeclaration(@PathVariable Long studentId) {
        declarationService.deleteStudentDeclarationById(studentId);
    }
}
