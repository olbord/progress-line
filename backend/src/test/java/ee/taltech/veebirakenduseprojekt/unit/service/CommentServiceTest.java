package ee.taltech.veebirakenduseprojekt.unit.service;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.entity.*;
import ee.taltech.veebirakenduseprojekt.mapper.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import ee.taltech.veebirakenduseprojekt.service.*;
import ee.taltech.veebirakenduseprojekt.exception.ApplicationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock private CommentRepository commentRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private CourseRepository courseRepository;

    @Spy private CommentMapper commentMapper;
    @Mock private StudentMapper studentMapper;

    @InjectMocks
    private CommentService commentService;

    private Course testCourse;
    private User testStudent;
    private Comment testComment1;
    private Comment testComment2;
    private CommentResponseDto dto1;
    private CommentResponseDto dto2;

    @BeforeEach
    void setUp() {
        testCourse = Course.builder()
                .id(35065L)
                .shortName("Kursus")
                .fullName("Testkursus")
                .build();

        testStudent = User.builder()
                .id(10L)
                .email("student@test.ee")
                .build();

        testComment1 = Comment.builder()
                .id(1L)
                .commentText("Esimene kommentaar")
                .type(CommentType.EMAIL)
                .course(testCourse)
                .student(testStudent)
                .build();

        testComment2 = Comment.builder()
                .id(2L)
                .commentText("Teine kommentaar")
                .type(CommentType.EMAIL)
                .course(testCourse)
                .student(testStudent)
                .build();

        dto1 = new CommentResponseDto();
        dto1.setId(1L);
        dto1.setCommentText("Esimene kommentaar");

        dto2 = new CommentResponseDto();
        dto2.setId(2L);
        dto2.setCommentText("Teine kommentaar");
    }

    @Test
    void getAllComments_whenNewCommentsAdded_returnsAllComments() {
        // Given
        List<Comment> commentsFromDb = List.of(testComment1, testComment2);
        given(commentRepository.findAllWithStudents()).willReturn(commentsFromDb);
        given(commentMapper.mapToResponseDto(testComment1)).willReturn(dto1);
        given(commentMapper.mapToResponseDto(testComment2)).willReturn(dto2);

        // When
        List<CommentResponseDto> result = commentService.getAllComments();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Esimene kommentaar", result.get(0).getCommentText());
        assertEquals("Teine kommentaar", result.get(1).getCommentText());

        then(commentRepository).should(times(1)).findAllWithStudents();
        then(commentMapper).should(times(2)).mapToResponseDto(any(Comment.class));
    }

    @Test
    void addComment_whenValidDto_savesAndReturnsResponse() {
        // Given
        CommentRequestDto request = new CommentRequestDto();
        request.setCommentText("Uus kommentaar");
        request.setStudentId(testStudent.getId());
        request.setCourseId(testCourse.getId());
        request.setType(CommentType.EMAIL);

        Comment mappedEntity = Comment.builder()
                .commentText("Uus kommentaar")
                .type(CommentType.EMAIL)
                .course(testCourse)
                .student(testStudent)
                .build();

        Comment saved = Comment.builder()
                .id(33L)
                .commentText("Uus kommentaar")
                .type(CommentType.EMAIL)
                .course(testCourse)
                .student(testStudent)
                .build();

        given(studentMapper.studentIdToEntity(testStudent.getId(), studentRepository))
                .willReturn(testStudent);
        given(commentMapper.mapToEntity(request, studentRepository, studentMapper, courseRepository))
                .willReturn(mappedEntity);
        given(commentRepository.save(mappedEntity)).willReturn(saved);
        given(commentMapper.mapToResponseDto(saved)).willReturn(dto1);

        // When
        CommentResponseDto result = commentService.addComment(request);

        // Then
        assertNotNull(result);
        assertEquals(dto1.getId(), result.getId());
        assertEquals(dto1.getCommentText(), result.getCommentText());

        then(studentMapper).should(times(1))
                .studentIdToEntity(testStudent.getId(), studentRepository);
        then(commentMapper).should(times(1))
                .mapToEntity(request, studentRepository, studentMapper, courseRepository);
        then(commentRepository).should(times(1)).save(mappedEntity);
        then(commentMapper).should(times(1)).mapToResponseDto(saved);
    }

    @Test
    void getCommentsByCourseIdAndStudentId_whenStudentDoesNotExist_throwsException() {
        // Given
        given(studentRepository.existsById(10L)).willReturn(false);

        // When + Then
        ApplicationException ex = assertThrows(ApplicationException.class,
                () -> commentService.getCommentsByCourseIdAndStudentId(35065L, 10L));

        assertEquals("Student not found with id 10", ex.getMessage());

        then(commentRepository).shouldHaveNoInteractions();
    }

    @Test
    void getCommentsByCourseIdAndStudentId_whenStudentExists_returnsMappedList() {
        // Given
        given(studentRepository.existsById(10L)).willReturn(true);
        given(commentRepository.findByStudentIdAndCourseId(35065L, 10L))
                .willReturn(List.of(testComment1, testComment2));

        given(commentMapper.mapToResponseDto(testComment1)).willReturn(dto1);
        given(commentMapper.mapToResponseDto(testComment2)).willReturn(dto2);

        // When
        List<CommentResponseDto> result =
                commentService.getCommentsByCourseIdAndStudentId(35065L, 10L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Esimene kommentaar", result.get(0).getCommentText());
        assertEquals("Teine kommentaar", result.get(1).getCommentText());

        then(commentRepository).should(times(1))
                .findByStudentIdAndCourseId(35065L, 10L);
        then(commentMapper).should(times(2)).mapToResponseDto(any(Comment.class));
    }
}
