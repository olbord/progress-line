package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.entity.*;
import ee.taltech.veebirakenduseprojekt.exception.*;
import ee.taltech.veebirakenduseprojekt.mapper.CourseMapper;
import ee.taltech.veebirakenduseprojekt.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final InactiveService inactiveService;
    private final CommentService commentService;
    private final CourseMapper courseMapper;
    private final GradeItemService gradeItemService;
    private final EmailService emailService;
    private final EmailRepository emailRepository;

    public List<CourseDto> getCourses(String teacherEmail, CourseRequestDto request) {
        if (teacherEmail == null || teacherEmail.isEmpty()) {
            throw new IllegalArgumentException("Authenticated teacher email must be provided");
        }

        String query = request != null ? request.getQuery() : null;
        String trimmedQuery = (query != null) ? query.trim() : null;

        List<Course> courses = courseRepository.searchCoursesByEmail(teacherEmail, trimmedQuery);
        return courseMapper.toDtoList(courses);
    }

    public Page<StudentDto> getPaginatedActiveStudentsWithDeclarations(Long courseId, String search, Pageable pageable) {
        validateCourseExists(courseId);
        return studentService.getPaginatedActiveStudentsWithDeclarationByCourse(courseId, search, pageable);
    }

    public Page<StudentDto> getPaginatedStudentsWithoutDeclarations(Long courseId, Pageable pageable) {
        validateCourseExists(courseId);
        return studentService.getPaginatedStudentsWithoutDeclarationByCourse(courseId, pageable);
    }

    public Page<StudentDto> getPaginatedInactiveStudents(Long courseId, Pageable pageable) {
        validateCourseExists(courseId);
        return studentService.getPaginatedInactiveStudentsWithDeclarationByCourse(courseId, pageable);
    }

    public void markStudentInactive(Long courseId, Long studentId) {
        validateCourseExists(courseId);
        inactiveService.addInactive(courseId, studentId);
    }

    public void markStudentActive(Long courseId, Long studentId) {
        validateCourseExists(courseId);
        inactiveService.deleteInactive(courseId, studentId);
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

    @Transactional
    public void sendAndSaveEmail(Long courseId, Long studentId, String emailBody, String senderEmail) {
        User teacherEntity = teacherService.getTeacherByEmail(senderEmail);

        StudentPersonalDataResponseDto studentInfo = studentService.getStudentPersonalDataById(studentId);
        String recipientEmail = studentInfo.getEmail();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ApplicationException("Kursust ID-ga " + courseId + " ei leitud."));
        String courseName = course.getFullName();

        String subject = "Tagasiside kursusel " + courseName;

        emailService.sendMail(recipientEmail, senderEmail, subject, emailBody);

        // Add new email to database table email
        Email email = Email.builder()
                .studentId(studentId)
                .courseId(courseId)
                .teacherId(teacherEntity.getId())
                .body(emailBody)
                .sentAt(LocalDateTime.now())
                .build();
        emailRepository.save(email);

        // Add comment to database table comment
        String commentText = "Saadetud email: \n\n" + emailBody.substring(0, Math.min(emailBody.length(), 250)) + "...\n\n";
        CommentRequestDto comment = CommentRequestDto.builder()
                .studentId(studentId)
                .commentText(commentText)
                .type(CommentType.EMAIL)
                .teacherId(teacherEntity.getId())
                .courseId(courseId)
                .build();
        commentService.addComment(comment);
    }
}
