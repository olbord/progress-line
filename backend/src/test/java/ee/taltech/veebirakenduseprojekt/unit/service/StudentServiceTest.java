package ee.taltech.veebirakenduseprojekt.unit.service;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.entity.StudentCourseId;
import ee.taltech.veebirakenduseprojekt.entity.StudentView;
import ee.taltech.veebirakenduseprojekt.entity.User;
import ee.taltech.veebirakenduseprojekt.mapper.StudentMapper;
import ee.taltech.veebirakenduseprojekt.repository.StudentRepository;
import ee.taltech.veebirakenduseprojekt.repository.StudentViewRepository;
import ee.taltech.veebirakenduseprojekt.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private StudentViewRepository studentViewRepository;
    @Mock private StudentMapper studentMapper;

    @InjectMocks private StudentService studentService;

    private StudentView studentView;
    private StudentCourseId studentCourseId;
    private StudentDto studentDto;
    private StudentPersonalDataResponseDto studentPersonalData;

    @BeforeEach
    void setUp() {
        studentCourseId = new StudentCourseId();
        studentCourseId.setUserId(1L);
        studentCourseId.setCourseId(100L);

        studentView = new StudentView();
        studentView.setId(studentCourseId);
        studentView.setFirstName("John");
        studentView.setLastName("Doe");
        studentView.setEmail("student@test.ee");

        studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setEmail("student@test.ee");

        studentPersonalData = new StudentPersonalDataResponseDto();
        studentPersonalData.setId(1L);
        studentPersonalData.setEmail("student@test.ee");
    }

    @Test
    void getPaginatedStudentsWithoutDeclarationByCourse_returnsEmptyPageWhenNoUsers() {
        given(studentRepository.findAll(ArgumentMatchers.<Specification<User>>any())).willReturn(List.of());

        Page<StudentDto> result = studentService.getPaginatedStudentsWithoutDeclarationByCourse(100L, "search", Pageable.unpaged());

        assertTrue(result.isEmpty());
        then(studentViewRepository).shouldHaveNoInteractions();
    }

    @Test
    void getPaginatedStudentsWithoutDeclarationByCourse_mapsResults() {
        given(studentRepository.findAll(ArgumentMatchers.<Specification<User>>any())).willReturn(List.of(new User() {{ setId(1L); }}));

        Page<StudentView> viewPage = new PageImpl<>(List.of(studentView));
        given(studentViewRepository.findByIdUserIdInAndIdCourseId(List.of(1L), 100L, Pageable.unpaged()))
                .willReturn(viewPage);
        given(studentMapper.mapFromView(studentView)).willReturn(studentDto);

        Page<StudentDto> result = studentService.getPaginatedStudentsWithoutDeclarationByCourse(100L, "search", Pageable.unpaged());

        assertEquals(1, result.getContent().size());
        assertEquals(studentDto, result.getContent().getFirst());
    }

    @Test
    void getPaginatedActiveStudentsWithDeclarationByCourse_returnsEmptyPageWhenNoUsers() {
        given(studentRepository.findAll(ArgumentMatchers.<Specification<User>>any())).willReturn(List.of());

        Page<StudentDto> result = studentService.getPaginatedActiveStudentsWithDeclarationByCourse(100L, "search", Pageable.unpaged());

        assertTrue(result.isEmpty());
        then(studentViewRepository).shouldHaveNoInteractions();
    }

    @Test
    void getPaginatedActiveStudentsWithDeclarationByCourse_mapsResults() {
        given(studentRepository.findAll(ArgumentMatchers.<Specification<User>>any())).willReturn(List.of(new User() {{ setId(1L); }}));

        Page<StudentView> viewPage = new PageImpl<>(List.of(studentView));
        given(studentViewRepository.findByIdUserIdInAndIdCourseId(List.of(1L), 100L, Pageable.unpaged()))
                .willReturn(viewPage);
        given(studentMapper.mapFromView(studentView)).willReturn(studentDto);

        Page<StudentDto> result = studentService.getPaginatedActiveStudentsWithDeclarationByCourse(100L, "search", Pageable.unpaged());

        assertEquals(1, result.getContent().size());
        assertEquals(studentDto, result.getContent().getFirst());
    }

    @Test
    void getPaginatedInactiveStudentsWithDeclarationByCourse_returnsEmptyPageWhenNoUsers() {
        given(studentRepository.findAll(ArgumentMatchers.<Specification<User>>any())).willReturn(List.of());

        Page<StudentDto> result = studentService.getPaginatedInactiveStudentsWithDeclarationByCourse(100L, "search", Pageable.unpaged());

        assertTrue(result.isEmpty());
        then(studentViewRepository).shouldHaveNoInteractions();
    }

    @Test
    void getPaginatedInactiveStudentsWithDeclarationByCourse_mapsResults() {
        given(studentRepository.findAll(ArgumentMatchers.<Specification<User>>any())).willReturn(List.of(new User() {{ setId(1L); }}));

        Page<StudentView> viewPage = new PageImpl<>(List.of(studentView));
        given(studentViewRepository.findByIdUserIdInAndIdCourseId(List.of(1L), 100L, Pageable.unpaged()))
                .willReturn(viewPage);
        given(studentMapper.mapFromView(studentView)).willReturn(studentDto);

        Page<StudentDto> result = studentService.getPaginatedInactiveStudentsWithDeclarationByCourse(100L, "search", Pageable.unpaged());

        assertEquals(1, result.getContent().size());
        assertEquals(studentDto, result.getContent().getFirst());
    }

    @Test
    void getStudentPersonalDataById_returnsData() {
        User user = new User();
        user.setId(1L);

        given(studentRepository.findById(1L)).willReturn(Optional.of(user));
        given(studentMapper.mapToPersonalDataResponseDto(user)).willReturn(studentPersonalData);

        StudentPersonalDataResponseDto result = studentService.getStudentPersonalDataById(1L);

        assertEquals(studentPersonalData, result);
    }

    @Test
    void getStudentPersonalDataById_throwsWhenNotFound() {
        given(studentRepository.findById(1L)).willReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> studentService.getStudentPersonalDataById(1L));
        assertTrue(ex.getMessage().contains("Student not found"));
    }

    @Test
    void getStudentStatus_returnsCorrectStatus() {
        User user = new User();
        given(studentRepository.findWithDeclarationByCourseAndId(100L, 1L)).willReturn(Optional.of(user));
        given(studentRepository.findInactiveStudentByCourseAndId(100L, 1L)).willReturn(Optional.empty());

        StudentStatusDto statusDto = StudentStatusDto.builder()
                .studentId(1L)
                .courseId(100L)
                .active(true)
                .declared(true)
                .build();

        given(studentMapper.mapStudentStatusDto(1L, 100L, true, true)).willReturn(statusDto);

        StudentStatusDto result = studentService.getStudentStatus(100L, 1L);

        assertEquals(statusDto, result);
        then(studentMapper).should(times(1)).mapStudentStatusDto(1L, 100L, true, true);
    }
}
