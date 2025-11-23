package ee.taltech.veebirakenduseprojekt.unit.controller;

import ee.taltech.veebirakenduseprojekt.controller.CourseController;
import ee.taltech.veebirakenduseprojekt.dto.CourseDto;
import ee.taltech.veebirakenduseprojekt.dto.CourseRequestDto;
import ee.taltech.veebirakenduseprojekt.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private CourseDto dto1;
    private CourseDto dto2;

    @BeforeEach
    void setUp() {
        dto1 = new CourseDto();
        dto1.setId(35065L);
        dto1.setShortName("ITI0102-2025");
        dto1.setFullName("Programmeerimise algkursus");

        dto2 = new CourseDto();
        dto2.setId(12345L);
        dto2.setShortName("TEST");
        dto2.setFullName("Testkursus");
    }

    @Test
    void getCourses_whenUserHasAccess_returnsFilteredList() {
        // Given
        String email = "dummyemail@example.com";
        CourseRequestDto searchRequest = new CourseRequestDto();

        given(courseService.getCourses(eq(email), any(CourseRequestDto.class)))
                .willReturn(List.of(dto1, dto2));

        // When
        List<CourseDto> result = courseController.getCourses(email, searchRequest);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ITI0102-2025", result.get(0).getShortName());

        then(courseService).should(times(1))
                .getCourses(eq(email), any(CourseRequestDto.class));
    }
}

