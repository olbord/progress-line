package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
class DeclarationControllerIntegrationTest extends AbstractIntegrationTest {

    private static final long TEST_COURSE_ID = 35065L;

    @Autowired
    private MockMvc mvc;

    // ----------------------------------------------------------------------
    // POST /api/courses/{courseId}/declarations/student/{studentId}
    // ----------------------------------------------------------------------
    @Test
    void addStudentDeclaration_whenStudentHasNoDeclaration_addsDeclaration() throws Exception {

        long studentId = getUserId("d.deklaratsioonita@taltech.ee");

        mvc.perform(post("/api/courses/{courseId}/declarations/student/{studentId}",
                        TEST_COURSE_ID, studentId))
                .andDo(print())
                .andExpect(status().isNoContent());

        // Verify DB state
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM declaration WHERE user_id = ? AND course_id = ?",
                Integer.class,
                studentId, TEST_COURSE_ID
        );

        assert count != null && count == 1;
    }

    // ----------------------------------------------------------------------
    // POST — already declared → BAD REQUEST
    // ----------------------------------------------------------------------
    @Test
    void addStudentDeclaration_whenStudentAlreadyDeclared_returnsBadRequest() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee"); // already declared

        mvc.perform(post("/api/courses/{courseId}/declarations/student/{studentId}",
                        TEST_COURSE_ID, studentId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // ----------------------------------------------------------------------
    // DELETE /api/courses/{courseId}/declarations/student/{studentId}/no-declaration
    // ----------------------------------------------------------------------
    @Test
    void deleteStudentDeclaration_whenStudentHasDeclaration_deletesDeclaration() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee"); // declared

        mvc.perform(delete("/api/courses/{courseId}/declarations/student/{studentId}/no-declaration",
                        TEST_COURSE_ID, studentId))
                .andDo(print())
                .andExpect(status().isNoContent());

        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM declaration WHERE user_id = ? AND course_id = ?",
                Integer.class,
                studentId, TEST_COURSE_ID
        );

        assert count != null && count == 0;
    }


    // ----------------------------------------------------------------------
    // Helper method: Lookup user ID by email
    // ----------------------------------------------------------------------
    @Autowired
    private JdbcTemplate jdbc;

    private long getUserId(String email) {
        return jdbc.queryForObject(
                "SELECT id FROM \"user\" WHERE email = ?",
                Long.class,
                email
        );
    }
}
