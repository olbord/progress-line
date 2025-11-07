package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.mapper.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    private Course testCourse;
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

        testComment1 = Comment.builder()
                .id(1L)
                .commentText("Esimene kommentaar")
                .type(CommentType.EMAIL)
                .course(testCourse)
                .build();

        testComment2 = Comment.builder()
                .id(1L)
                .commentText("Esimene kommentaar")
                .type(CommentType.EMAIL)
                .course(testCourse)
                .build();

        dto1 = new CommentResponseDto();
        dto1.setId(1L);
        dto1.setCommentText("Esimene kommentaar");

        dto2 = new CommentResponseDto();
        dto2.setId(2L);
        dto2.setCommentText("Teine kommentaar");
    }

    @Test
    void getAllComments_newCommentsAdded_returnsAllComments() {
        // Given
        List<Comment> commentsFromDb = List.of(testComment1, testComment2);
        when(commentRepository.findAllWithStudents()).thenReturn(commentsFromDb);
        when(commentMapper.mapToResponseDto(testComment1)).thenReturn(dto1);
        when(commentMapper.mapToResponseDto(testComment2)).thenReturn(dto2);

        // When
        List<CommentResponseDto> result = commentService.getAllComments();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size(), "Oodati kahte kommentaari, aga saadi " + result.size());
        assertEquals("Esimene kommentaar", result.get(0).getCommentText());
        assertEquals("Teine kommentaar", result.get(1).getCommentText());
        verify(commentRepository, times(1)).findAllWithStudents();
        verify(commentMapper, times(2)).mapToResponseDto(any(Comment.class));
    }
}