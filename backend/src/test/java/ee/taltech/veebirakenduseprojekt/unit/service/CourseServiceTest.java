package ee.taltech.veebirakenduseprojekt.unit.service;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.entity.*;
import ee.taltech.veebirakenduseprojekt.mapper.CourseMapper;
import ee.taltech.veebirakenduseprojekt.repository.*;
import ee.taltech.veebirakenduseprojekt.service.*;
import ee.taltech.veebirakenduseprojekt.exception.ApplicationException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private StudentService studentService;
    @Mock private TeacherService teacherService;
    @Mock private InactiveService inactiveService;
    @Mock private CommentService commentService;
    @Mock private CourseMapper courseMapper;
    @Mock private GradeItemService gradeItemService;
    @Mock private EmailService emailService;
    @Mock private EmailRepository emailRepository;

    @InjectMocks private CourseService courseService;

    private Course testCourse;
    private User testTeacher;
    private StudentPersonalDataResponseDto testStudentData;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(100L);
        testCourse.setFullName("Test Course");

        testTeacher = new User();
        testTeacher.setId(1L);
        testTeacher.setEmail("teacher@test.ee");

        testStudentData = new StudentPersonalDataResponseDto();
        testStudentData.setId(10L);
        testStudentData.setEmail("student@test.ee");
    }

    @Test
    void getCourses_whenEmailNull_throwsIllegalArgumentException() {
        CourseRequestDto request = new CourseRequestDto();
        request.setQuery("some");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> courseService.getCourses(null, request));

        assertEquals("Authenticated teacher email must be provided", ex.getMessage());
    }

    @Test
    void getCourses_trimsQueryAndReturnsDtoList() {
        CourseRequestDto request = new CourseRequestDto();
        request.setQuery("  testquery  ");

        List<Course> courses = List.of(testCourse);
        List<CourseDto> dtos = List.of(new CourseDto());

        given(courseRepository.searchCoursesByEmail("teacher@test.ee", "testquery"))
                .willReturn(courses);
        given(courseMapper.toDtoList(courses)).willReturn(dtos);

        List<CourseDto> result = courseService.getCourses("teacher@test.ee", request);

        assertEquals(1, result.size());
        then(courseRepository).should(times(1))
                .searchCoursesByEmail("teacher@test.ee", "testquery");
        then(courseMapper).should(times(1)).toDtoList(courses);
    }

    @Test
    void getPaginatedActiveStudentsWithDeclarations_callsService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);
        Page<StudentDto> page = Page.empty();
        given(studentService.getPaginatedActiveStudentsWithDeclarationByCourse(100L, "search", Pageable.unpaged()))
                .willReturn(page);

        Page<StudentDto> result = courseService.getPaginatedActiveStudentsWithDeclarations(100L, "search", Pageable.unpaged());

        assertEquals(page, result);
        then(studentService).should(times(1))
                .getPaginatedActiveStudentsWithDeclarationByCourse(100L, "search", Pageable.unpaged());
    }

    @Test
    void getPaginatedActiveStudentsWithDeclarations_whenCourseNotExist_throws() {
        given(courseRepository.existsById(100L)).willReturn(false);

        Pageable pageable = Pageable.unpaged();

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> courseService.getPaginatedActiveStudentsWithDeclarations(100L, "search", pageable)
        );

        assertTrue(ex.getMessage().contains("Course with id 100 not found"));
        then(studentService).shouldHaveNoInteractions();
    }


    @Test
    void getPaginatedStudentsWithoutDeclarations_callsService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);
        Page<StudentDto> page = Page.empty();
        given(studentService.getPaginatedStudentsWithoutDeclarationByCourse(100L, "search", Pageable.unpaged()))
                .willReturn(page);

        Page<StudentDto> result = courseService.getPaginatedStudentsWithoutDeclarations(100L, "search", Pageable.unpaged());

        assertEquals(page, result);
        then(studentService).should(times(1))
                .getPaginatedStudentsWithoutDeclarationByCourse(100L, "search", Pageable.unpaged());
    }

    @Test
    void getPaginatedInactiveStudents_callsService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);
        Page<StudentDto> page = Page.empty();
        given(studentService.getPaginatedInactiveStudentsWithDeclarationByCourse(100L, "search", Pageable.unpaged()))
                .willReturn(page);

        Page<StudentDto> result = courseService.getPaginatedInactiveStudents(100L, "search", Pageable.unpaged());

        assertEquals(page, result);
        then(studentService).should(times(1))
                .getPaginatedInactiveStudentsWithDeclarationByCourse(100L, "search", Pageable.unpaged());
    }

    @Test
    void markStudentInactive_callsInactiveService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);

        courseService.markStudentInactive(100L, 10L);

        then(inactiveService).should(times(1)).addInactive(100L, 10L);
    }

    @Test
    void markStudentActive_callsInactiveService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);

        courseService.markStudentActive(100L, 10L);

        then(inactiveService).should(times(1)).deleteInactive(100L, 10L);
    }

    @Test
    void getCommentsByStudentId_callsCommentService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);
        List<CommentResponseDto> comments = List.of(new CommentResponseDto());
        given(commentService.getCommentsByCourseIdAndStudentId(100L, 10L)).willReturn(comments);

        List<CommentResponseDto> result = courseService.getCommentsByStudentId(100L, 10L);

        assertEquals(comments, result);
        then(commentService).should(times(1)).getCommentsByCourseIdAndStudentId(100L, 10L);
    }

    @Test
    void getAllStudentGradesByStudentId_callsGradeItemService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);
        List<GradeItemDto> grades = List.of(new GradeItemDto());
        given(gradeItemService.getAllStudentGradesByStudentId(100L, 10L)).willReturn(grades);

        List<GradeItemDto> result = courseService.getAllStudentGradesByStudentId(100L, 10L);

        assertEquals(grades, result);
        then(gradeItemService).should(times(1)).getAllStudentGradesByStudentId(100L, 10L);
    }

    @Test
    void getCourseProgressLineData_callsGradeItemService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);
        List<StudentProgressLineDto> data = List.of(new StudentProgressLineDto());
        given(gradeItemService.getCourseProgressLineData(100L)).willReturn(data);

        List<StudentProgressLineDto> result = courseService.getCourseProgressLineData(100L);

        assertEquals(data, result);
        then(gradeItemService).should(times(1)).getCourseProgressLineData(100L);
    }

    @Test
    void getTeachers_callsTeacherService_whenCourseExists() {
        given(courseRepository.existsById(100L)).willReturn(true);
        List<TeacherDto> teachers = List.of(new TeacherDto());
        given(teacherService.getTeachersByCourse(100L)).willReturn(teachers);

        List<TeacherDto> result = courseService.getTeachers(100L);

        assertEquals(teachers, result);
        then(teacherService).should(times(1)).getTeachersByCourse(100L);
    }

    @Test
    void sendAndSaveEmail_happyPath_sendsEmailAndAddsComment() {
        given(teacherService.getTeacherByEmail("teacher@test.ee")).willReturn(testTeacher);
        given(studentService.getStudentPersonalDataById(10L)).willReturn(testStudentData);
        given(courseRepository.findById(100L)).willReturn(Optional.of(testCourse));

        courseService.sendAndSaveEmail(100L, 10L, "Hello student", "teacher@test.ee");

        then(teacherService).should(times(1)).getTeacherByEmail("teacher@test.ee");
        then(studentService).should(times(1)).getStudentPersonalDataById(10L);
        then(courseRepository).should(times(1)).findById(100L);
        then(emailService).should(times(1)).sendMail("student@test.ee", "teacher@test.ee",
                "Tagasiside kursusel Test Course", "Hello student");
        then(emailRepository).should(times(1)).save(any());
        then(commentService).should(times(1)).addComment(any());
    }

    @Test
    void sendAndSaveEmail_whenCourseNotFound_throwsApplicationException() {
        given(teacherService.getTeacherByEmail("teacher@test.ee")).willReturn(testTeacher);
        given(studentService.getStudentPersonalDataById(10L)).willReturn(testStudentData);
        given(courseRepository.findById(100L)).willReturn(Optional.empty());

        ApplicationException ex = assertThrows(ApplicationException.class,
                () -> courseService.sendAndSaveEmail(100L, 10L, "Hello student", "teacher@test.ee"));

        assertTrue(ex.getMessage().contains("Kursust ID-ga 100 ei leitud"));
        then(emailService).shouldHaveNoInteractions();
        then(emailRepository).shouldHaveNoInteractions();
        then(commentService).shouldHaveNoInteractions();
    }
}
