package ee.taltech.veebirakenduseprojekt.unit.service;

import ee.taltech.veebirakenduseprojekt.dto.GradeItemDto;
import ee.taltech.veebirakenduseprojekt.dto.LastGradeDto;
import ee.taltech.veebirakenduseprojekt.dto.StudentProgressLineDto;
import ee.taltech.veebirakenduseprojekt.exception.ApplicationException;
import ee.taltech.veebirakenduseprojekt.mapper.GradeItemMapper;
import ee.taltech.veebirakenduseprojekt.entity.GradeItem;
import ee.taltech.veebirakenduseprojekt.entity.User;
import ee.taltech.veebirakenduseprojekt.repository.GradeItemRepository;
import ee.taltech.veebirakenduseprojekt.service.GradeItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeItemServiceTest {

    @Mock
    private GradeItemRepository gradeItemRepository;

    @Mock
    private GradeItemMapper gradeItemMapper;

    @InjectMocks
    private GradeItemService gradeItemService;

    private GradeItem gradeItem;
    private User student;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");

        gradeItem = new GradeItem();
        gradeItem.setId(1L);
        gradeItem.setUserId(student.getId());
        gradeItem.setGrade(new BigDecimal("95"));
        gradeItem.setStudent(student);
        gradeItem.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getLatestGrade_returnsDto_whenGradeExists() {
        when(gradeItemRepository.findTopByCourseIdAndUserIdOrderByCreatedAtDesc(1L, 1L))
                .thenReturn(gradeItem);
        when(gradeItemMapper.gradeItemEntityToLastGradeDto(gradeItem))
                .thenReturn(new LastGradeDto());

        LastGradeDto result = gradeItemService.getLatestGrade(1L, 1L);

        assertNotNull(result);
        verify(gradeItemRepository, times(1))
                .findTopByCourseIdAndUserIdOrderByCreatedAtDesc(1L, 1L);
        verify(gradeItemMapper, times(1))
                .gradeItemEntityToLastGradeDto(gradeItem);
    }

    @Test
    void getLatestGrade_throwsException_whenGradeDoesNotExist() {
        when(gradeItemRepository.findTopByCourseIdAndUserIdOrderByCreatedAtDesc(1L, 1L))
                .thenReturn(null);

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> gradeItemService.getLatestGrade(1L, 1L));
        assertEquals("No grade found for student with id 1", exception.getMessage());
    }

    @Test
    void getAllStudentGradesByStudentId_returnsList() {
        when(gradeItemRepository.findAllByCourseIdAndUserId(1L, 1L))
                .thenReturn(List.of(gradeItem));
        when(gradeItemMapper.mapToDto(gradeItem))
                .thenReturn(new GradeItemDto());

        List<GradeItemDto> result = gradeItemService.getAllStudentGradesByStudentId(1L, 1L);

        assertEquals(1, result.size());
        verify(gradeItemRepository, times(1))
                .findAllByCourseIdAndUserId(1L, 1L);
    }

    @Test
    void getCourseProgressLineData_returnsChartData() {
        when(gradeItemRepository.findProgressLineDataByCourseId(1L))
                .thenReturn(List.of(gradeItem));

        List<StudentProgressLineDto> chartData = gradeItemService.getCourseProgressLineData(1L);

        assertEquals(1, chartData.size());
        StudentProgressLineDto dto = chartData.getFirst();
        assertEquals(student.getId(), dto.getStudentId());
        assertEquals(student.getFirstName(), dto.getFirstName());
        assertEquals(student.getLastName(), dto.getLastName());

        assertEquals(1, dto.getDataPoints().size());
        assertEquals(gradeItem.getGrade(), dto.getDataPoints().getFirst().getY());
    }
}
