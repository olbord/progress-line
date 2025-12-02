package ee.taltech.veebirakenduseprojekt.unit.service;

import ee.taltech.veebirakenduseprojekt.entity.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import ee.taltech.veebirakenduseprojekt.service.InactiveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InactiveServiceTest {

    @Mock private InactiveRepository inactiveRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks private InactiveService inactiveService;

    private User student;
    private Course course;
    private Inactive inactive;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);

        course = new Course();
        course.setId(100L);

        inactive = new Inactive();
        inactive.setStudent(student);
        inactive.setCourse(course);
        inactive.setDateTime(LocalDateTime.now());
    }

    @Test
    void addInactive_savesInactiveSuccessfully() {
        given(studentRepository.findWithDeclarationByCourseAndId(100L, 1L))
                .willReturn(Optional.of(student));
        given(inactiveRepository.existsByCourseIdAndStudentId(100L, 1L)).willReturn(false);
        given(courseRepository.getReferenceById(100L)).willReturn(course);
        given(inactiveRepository.save(any(Inactive.class))).willReturn(inactive);

        Inactive result = inactiveService.addInactive(100L, 1L);

        assertEquals(student, result.getStudent());
        assertEquals(course, result.getCourse());
        assertNotNull(result.getDateTime());

        then(inactiveRepository).should(times(1)).save(any(Inactive.class));
    }

    @Test
    void addInactive_throwsIfStudentNotFound() {
        given(studentRepository.findWithDeclarationByCourseAndId(100L, 1L))
                .willReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> inactiveService.addInactive(100L, 1L));
        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void addInactive_throwsIfAlreadyInactive() {
        given(studentRepository.findWithDeclarationByCourseAndId(100L, 1L))
                .willReturn(Optional.of(student));
        given(inactiveRepository.existsByCourseIdAndStudentId(100L, 1L)).willReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> inactiveService.addInactive(100L, 1L));
        assertTrue(ex.getMessage().contains("already marked as inactive"));
    }

    @Test
    void deleteInactive_deletesSuccessfully() {
        given(inactiveRepository.existsByCourseIdAndStudentId(100L, 1L)).willReturn(true);

        inactiveService.deleteInactive(100L, 1L);

        then(inactiveRepository).should(times(1))
                .deleteInactiveByStudentIdAndCourseId(1L, 100L);
    }

    @Test
    void deleteInactive_throwsIfNotInactive() {
        given(inactiveRepository.existsByCourseIdAndStudentId(100L, 1L)).willReturn(false);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> inactiveService.deleteInactive(100L, 1L));
        assertTrue(ex.getMessage().contains("not marked as inactive"));
    }
}
