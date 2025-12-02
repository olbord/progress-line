package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.AbstractIntegrationTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class CourseTeacherControllerIntegrationTest extends AbstractIntegrationTestConfig {

    private static final long TEST_COURSE_ID = 35065L;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JdbcTemplate jdbc;

    // ----------------------------------------------------------------------
    // GET /api/courses/{courseId}/teachers
    // ----------------------------------------------------------------------

    @Test
    void getTeachersByCourse_whenCourseHasTeachers_returnsTeacherList() throws Exception {

        mvc.perform(get("/api/courses/{courseId}/teachers", TEST_COURSE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())

                // Must contain exactly 1 teacher
                .andExpect(jsonPath("$.length()").value(1))

                // Verify teacher fields
                .andExpect(jsonPath("$[0].email").value("kadi.jairus@taltech.ee"))
                .andExpect(jsonPath("$[0].firstName", notNullValue()))
                .andExpect(jsonPath("$[0].lastName", notNullValue()));
    }

    // ----------------------------------------------------------------------
    // Helper: Ensure teacher exists in DB
    // (normally unnecessary because Liquibase test dataset already provides it)
    // ----------------------------------------------------------------------
    @Test
    void teacherExistsInDatabase() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM \"user\" WHERE email = 'kadi.jairus@taltech.ee'",
                Integer.class
        );
        assertEquals(1, count);
    }
}
