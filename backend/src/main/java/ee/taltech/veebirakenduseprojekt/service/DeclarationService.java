package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.entity.*;
import ee.taltech.veebirakenduseprojekt.exception.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.*;



@Service
@RequiredArgsConstructor
public class DeclarationService {

    private final DeclarationRepository declarationRepository;

    public void deleteStudentDeclarationById(Long studentId, Long courseId) {
        declarationRepository.deleteDeclarationByStudentIdAndCourseId(studentId, courseId);
    }

    public void addStudentToDeclaration(Long courseId, Long studentId) {
        boolean alreadyExists = declarationRepository.existsByCourse_IdAndStudent_Id(courseId, studentId);
        if (alreadyExists) {
            throw new ApplicationException("Student already declared for this course");
        }
        Declaration declaration = new Declaration();
        Course course = new Course();
        course.setId(courseId);
        User student = new User();
        student.setId(studentId);
        declaration.setDateTime(LocalDateTime.now());
        declaration.setCourse(course);
        declaration.setStudent(student);
        declarationRepository.save(declaration);
    }
}
