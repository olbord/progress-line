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
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public void deleteStudentDeclarationById(Long studentId, Long courseId) {
        declarationRepository.deleteDeclarationByStudentIdAndCourseId(studentId, courseId);
    }

    public void addStudentToDeclaration(Long courseId, Long studentId) {
        boolean alreadyExists = declarationRepository.existsByCourse_IdAndStudent_Id(courseId, studentId);
        if (alreadyExists) {
            throw new ApplicationException("Student already declared for this course");
        }
        Declaration declaration = new Declaration();
        declaration.setDateTime(LocalDateTime.now());
        declaration.setCourse(courseRepository.getReferenceById(courseId));
        declaration.setStudent(studentRepository.getReferenceById(studentId));
        declarationRepository.save(declaration);
    }
}
