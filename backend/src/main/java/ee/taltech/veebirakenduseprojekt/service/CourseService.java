package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.mapper.CourseMapper;
import ee.taltech.veebirakenduseprojekt.repository.Course;
import ee.taltech.veebirakenduseprojekt.repository.CourseRepository;
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
    private final StudentService studentService;
    private final InactiveService inactiveService;
    private final CommentService commentService;
    private final CourseMapper courseMapper;
    private final GradeItemService gradeItemService;
    private final TeacherService teacherService;

    public List<CourseDto> getCourses(String teacherEmail, CourseSearchRequest request) {
        if (teacherEmail == null || teacherEmail.isEmpty()) {
            throw new IllegalArgumentException("Authenticated teacher email must be provided");
        }

        String query = request != null ? request.getQuery() : null;
        String trimmedQuery = (query != null) ? query.trim() : null;

        List<Course> courses = courseRepository.searchCoursesByEmail(teacherEmail, trimmedQuery);
        return courseMapper.toDtoList(courses);
    }

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

    public List<CommentResponseDto> getCommentsByStudentId(Long courseId, Long studentId) {
        validateCourseExists(courseId);
        return commentService.getCommentsByCourseIdAndStudentId(courseId, studentId);
    }

    public List<GradeItemDto> getAllStudentGradesByStudentId(Long courseId, Long studentId) {
        validateCourseExists(courseId);
        return gradeItemService.getAllStudentGradesByStudentId(courseId, studentId);
    }

    public List<StudentProgressLineDto> getCourseProgressLineData(Long courseId) {
        validateCourseExists(courseId);
        return gradeItemService.getCourseProgressLineData(courseId);
    }

    public List<TeacherDto> getTeachers(Long courseId) {
        validateCourseExists(courseId);
        return teacherService.getTeachersByCourse(courseId);
    }
}
