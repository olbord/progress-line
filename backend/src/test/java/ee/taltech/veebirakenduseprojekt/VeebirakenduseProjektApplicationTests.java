package ee.taltech.veebirakenduseprojekt;

import ee.taltech.veebirakenduseprojekt.repository.*;
import jakarta.transaction.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.*;
import org.springframework.test.web.servlet.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerTest extends AbstractIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "kadi.jairus@taltech.ee")
    void getCourses_whenUserIsTeacher_returnsCoursesArray() throws Exception {
        mvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(35065))
                .andExpect(jsonPath("$[0].shortName").value("ITI0102-2025"))
                .andExpect(jsonPath("$[0].fullName").value("ITI0102 Programmeerimise algkursus (2025/2026)"));
        assertThat(courseRepository).isNotNull();
    }

}
