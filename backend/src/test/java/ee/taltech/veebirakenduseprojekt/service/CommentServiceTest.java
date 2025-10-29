package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.mapper.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mapstruct.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        User testStudent = User.builder
    }

    @Test
    void getAllComments() {
        Course testCourse = new Course("Pythoni_kurus", )
        //Given
        //When
        //Then
    }

    @Test
    void getComment() {
    }

    @Test
    void addComment() {
    }

    @Test
    void updateComment() {
    }

    @Test
    void getCommentsByCourseIdAndStudentId() {
    }
}