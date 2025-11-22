package ee.taltech.veebirakenduseprojekt.unit.controller;

import ee.taltech.veebirakenduseprojekt.controller.CourseTeacherController;
import ee.taltech.veebirakenduseprojekt.dto.TeacherDto;
import ee.taltech.veebirakenduseprojekt.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CourseTeacherControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseTeacherController controller;

    private TeacherDto t1;
    private TeacherDto t2;

    @BeforeEach
    void setUp() {
        t1 = new TeacherDto();
        t1.setFirstName("Kadi");
        t1.setLastName("Jairus");
        t1.setEmail("kadi.jairus@taltech.ee");

        t2 = new TeacherDto();
        t2.setFirstName("John");
        t2.setLastName("Doe");
        t2.setEmail("john.doe@taltech.ee");
    }

    // ----------------------------------------------------------------------
    // GET /api/courses/{courseId}/teachers
    // ----------------------------------------------------------------------

    @Test
    void getTeachersByCourse_whenCourseHasTeachers_returnsTeacherList() {
        // Given
        Long courseId = 35065L;

        given(courseService.getTeachers(courseId))
                .willReturn(List.of(t1, t2));

        // When
        List<TeacherDto> result = controller.getTeachersByCourse(courseId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("kadi.jairus@taltech.ee", result.get(0).getEmail());

        then(courseService).should(times(1)).getTeachers(courseId);
    }

    // ----------------------------------------------------------------------
    // GET /api/courses/{courseId}/teachers
    // ----------------------------------------------------------------------

    @Test
    void getTeachersByCourse_whenNoTeachers_returnsEmptyList() {
        // Given
        Long courseId = 123L;

        given(courseService.getTeachers(courseId))
                .willReturn(List.of());

        // When
        List<TeacherDto> result = controller.getTeachersByCourse(courseId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        then(courseService).should(times(1)).getTeachers(courseId);
    }
}

