package ee.taltech.veebirakenduseprojekt.unit.controller;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.service.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CourseStudentControllerTest {

    @Mock
    private CourseService courseService;

    @Mock
    private CourseAccessService accessService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private CourseStudentController controller;
    private StudentPersonalDataResponseDto personalData;
    private StudentDto s1;
    private StudentDto s2;

    @BeforeEach
    void setUp() {
        personalData = new StudentPersonalDataResponseDto();
        personalData.setId(10L);
        personalData.setFirstName("Anna");
        personalData.setLastName("Aktiivne");
        personalData.setEmail("a.aktiivne@taltech.ee");

        s1 = new StudentDto();
        s1.setId(10L);
        s1.setEmail("a.aktiivne@taltech.ee");

        s2 = new StudentDto();
        s2.setId(20L);
        s2.setEmail("d.deklaratsioonita@taltech.ee");
    }

    @Test
    void getStudentOnCourse_whenUserHasAccess_returnsStudentData() {
        // Given
        Long courseId = 35065L;
        Long studentId = 10L;
        String email = "teacher@example.com";

        given(studentService.getStudentPersonalDataById(studentId))
                .willReturn(personalData);

        // When
        StudentPersonalDataResponseDto response =
                controller.getStudentOnCourse(courseId, studentId, email);

        // Then
        assertNotNull(response);
        assertEquals("a.aktiivne@taltech.ee", response.getEmail());

        then(accessService).should(times(1)).requireTeacherAccess(email, courseId);
        then(studentService).should(times(1)).getStudentPersonalDataById(studentId);
    }

    @Test
    void getActiveStudentsWithDeclarations_whenValidRequest_returnsPage() {
        // Given
        Long courseId = 35065L;
        String email = "teacher@example.com";

        Page<StudentDto> page = new PageImpl<>(List.of(s1, s2));

        given(courseService.getPaginatedActiveStudentsWithDeclarations(eq(courseId), eq(""), any(Pageable.class)))
                .willReturn(page);

        // When
        Page<StudentDto> result =
                controller.getActiveStudentsWithDeclarations(courseId, email, 0, 10, "", "firstName,asc");

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());

        then(accessService).should().requireTeacherAccess(email, courseId);
        then(courseService).should().getPaginatedActiveStudentsWithDeclarations(eq(courseId), eq(""), any(Pageable.class));
    }

    @Test
    void markInactive_whenStudentActive_returnsCreated() {
        // Given
        Long courseId = 35065L;
        Long studentId = 10L;
        String email = "teacher@example.com";

        // No exception → means success

        // When
        ResponseEntity<String> response =
                controller.markInactive(courseId, studentId, email);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Student marked as inactive successfully.", response.getBody());

        then(accessService).should().requireTeacherAccess(email, courseId);
        then(courseService).should().markStudentInactive(courseId, studentId);
    }

}
