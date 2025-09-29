package ee.taltech.veebirakenduseprojekt.controller;

import ee.taltech.veebirakenduseprojekt.service.GradeItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/grades")
public class GradeItemController {

    private final GradeItemService gradeItemService;

    @GetMapping("/students/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GradeItemDto> getGratesItemsByStudent(@PathVariable Long studentId) {
        return gradeItemService.getAllStudentGradesByStudentId(studentId);
    }
}

