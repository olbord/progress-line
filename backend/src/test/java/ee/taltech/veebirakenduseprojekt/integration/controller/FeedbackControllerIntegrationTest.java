package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
class FeedbackControllerIntegrationTest extends AbstractIntegrationTest {

    private static final long TEST_COURSE_ID = 35065L;

    @Autowired
    private MockMvc mvc;

    // ======================================================================
    // 1) GET — student has exactly 4 responses (from test-data)
    // ======================================================================
    @Test
    void getFeedback_studentHasFourResponses() throws Exception {

        long studentId = getUserId("h.mittesellelkursuseltinaktiivneeklaratsioonita@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/feedback",
                        TEST_COURSE_ID, studentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))

                // Check first feedback block
                .andExpect(jsonPath("$[0].feedbackName", is("Iganädalane tagasiside - 1. nädal")))
                .andExpect(jsonPath("$[0].responseId", is(2)))
                .andExpect(jsonPath("$[0].questionName", is("Milline on aine tempo?")))
                .andExpect(jsonPath("$[0].printValue", is("Paras")));

    }


    // ======================================================================
    // 2) GET — student from same course but no responses → empty list
    // ======================================================================
    @Test
    void getFeedback_studentHasNoResponses_returnsEmptyList() throws Exception {

        // Choose any user that has NO feedback attempts
        long studentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/feedback",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ======================================================================
    // 3) GET — student is NOT part of this course → empty list
    // ======================================================================
    @Test
    void getFeedback_studentFromOtherCourse_returnsEmpty() throws Exception {

        long studentId = getUserId("g.mittesellelkursuseltaktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/feedback",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
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
