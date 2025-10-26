package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InactiveService {

    private final InactiveRepository inactiveRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public Inactive addInactive(Long courseId, Long studentId) {

        Student student = studentRepository.findWithDeclarationByCourseAndId(courseId, studentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Student not found or has not declared the course."
                ));

        if (inactiveRepository.existsByCourseIdAndStudentId(courseId, studentId)) {
            throw new IllegalStateException("Student is already marked as inactive.");
        }

        Inactive inactive = new Inactive();
        inactive.setStudent(student);
        inactive.setCourse(courseRepository.getReferenceById(courseId));
        inactive.setDateTime(LocalDateTime.now());

        return inactiveRepository.save(inactive);
    }
}
