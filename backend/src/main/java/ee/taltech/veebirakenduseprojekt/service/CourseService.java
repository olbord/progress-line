package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.CommentResponseDto;
import ee.taltech.veebirakenduseprojekt.controller.StudentDto;
import ee.taltech.veebirakenduseprojekt.exception.ApplicationException;
import ee.taltech.veebirakenduseprojekt.mapper.CommentMapper;
import ee.taltech.veebirakenduseprojekt.repository.CommentRepository;
import ee.taltech.veebirakenduseprojekt.repository.CourseRepository;
import ee.taltech.veebirakenduseprojekt.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final StudentService studentService;
    private final InactiveService inactiveService;

    public List<StudentDto> getAllActiveStudentsWithDeclarations(Long courseId) {
        validateCourseExists(courseId);
        return studentService.getAllActiveStudentsWithDeclarationByCourse(courseId);
    }

    public List<StudentDto> getAllStudentsWithoutDeclarations(Long courseId) {
        validateCourseExists(courseId);
        return studentService.getAllStudentsWithoutDeclarationByCourse(courseId);
    }

    public List<StudentDto> getAllInactiveStudents(Long courseId) {
        validateCourseExists(courseId);
        return studentService.getAllInactiveStudentsWithDeclarationByCourse(courseId);
    }

    public void markStudentInactive(Long courseId, Long studentId) {
        validateCourseExists(courseId);
        inactiveService.addInactive(courseId, studentId);
    }

    private void validateCourseExists(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course with id " + courseId + " not found.");
        }
    }

    public List<CommentResponseDto> getCommentsByCourseIdAndStudentId(Long courseId, Long studentId) {
        validateCourseExists(courseId);
        log.debug("Getting comments of student with id " + studentId);
        if (!studentRepository.existsById(studentId)) {
            throw new ApplicationException("Student not found with id " + studentId);
        }
        return commentRepository.findByStudentIdAndCourseId(courseId, studentId)
                .stream()
                .map(commentMapper::mapToResponseDto)
                .toList();
    }
}
