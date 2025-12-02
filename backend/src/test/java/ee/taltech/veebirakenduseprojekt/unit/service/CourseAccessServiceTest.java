package ee.taltech.veebirakenduseprojekt.unit.service;

import ee.taltech.veebirakenduseprojekt.repository.TeacherRepository;
import ee.taltech.veebirakenduseprojekt.service.CourseAccessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CourseAccessServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CourseAccessService courseAccessService;

    @Test
    void requireTeacherAccess_whenTeacherHasAccess_doesNotThrow() {
        // Given
        String email = "teacher@test.ee";
        Long courseId = 123L;

        given(teacherRepository.isTeacherOfCourse(email, courseId)).willReturn(true);

        // When + Then
        assertDoesNotThrow(() -> courseAccessService.requireTeacherAccess(email, courseId));

        // Repository interaction
        then(teacherRepository).should(times(1)).isTeacherOfCourse(email, courseId);
    }

    @Test
    void requireTeacherAccess_whenTeacherHasNoAccess_throwsForbidden() {
        // Given
        String email = "intruder@test.ee";
        Long courseId = 123L;

        given(teacherRepository.isTeacherOfCourse(email, courseId)).willReturn(false);

        // When
        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> courseAccessService.requireTeacherAccess(email, courseId)
        );

        // Then
        assertEquals(403, ex.getStatusCode().value());
        assertTrue(Objects.requireNonNull(ex.getReason()).contains("Not a teacher of this course"));

        then(teacherRepository).should(times(1)).isTeacherOfCourse(email, courseId);
    }
}
