package ee.taltech.veebirakenduseprojekt.integration;

import ee.taltech.veebirakenduseprojekt.repository.*;
import jakarta.transaction.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;
import org.springframework.test.context.jdbc.*;
import org.springframework.test.web.servlet.*;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
class CourseControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private MockMvc mvc;

    @Test
    void getCourses_whenUserIsTeacher_returnsCoursesArray() throws Exception {
        mvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(35065))
                .andExpect(jsonPath("$[0].shortName").value("ITI0102-2025"))
                .andExpect(jsonPath("$[0].fullName").value("ITI0102 Programmeerimise algkursus (2025/2026)"));
        assertThat(courseRepository).isNotNull();
    }

    @Test
    @Sql(statements = {
            "INSERT INTO course (id, short_name, full_name) VALUES (666, 'Other course', 'Other Course without teacher access');"
    })
    void getCourses_whenNewCourseIsAddes_returnsSingleCourseArray() throws Exception {
        mvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[*].id").value(not(hasItem(666L))))
                .andExpect(jsonPath("$[*].id").value(hasItem(35065)));
    }

}