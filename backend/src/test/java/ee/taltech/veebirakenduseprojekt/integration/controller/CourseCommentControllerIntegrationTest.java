package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.AbstractIntegrationTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
class CourseCommentControllerIntegrationTest extends AbstractIntegrationTest {

    private static final long TEST_COURSE_ID = 35065L;

    @Autowired private MockMvc mvc;

    // ======================================================================
    // 1) GET — student a.aktiivne has two comments
    // ======================================================================
    @Test
    void getComments_studentHasTwoComments() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/comments",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", is(2)))

                // Newest first
                .andExpect(jsonPath("$[0].commentText", is("Teine meeldetuletus.")))
                .andExpect(jsonPath("$[1].commentText", is("Meeldetuletus. Parandatud kuupäev.")))

                // Student info
                .andExpect(jsonPath("$[0].student.id", is((int) studentId)))
                .andExpect(jsonPath("$[0].student.email", is("a.aktiivne@taltech.ee")));
    }

    // ======================================================================
    // 2) GET — student i.inaktiivne has one comment
    // ======================================================================
    @Test
    void getComments_studentHasOneComment() throws Exception {

        long studentId = getUserId("i.inaktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/comments",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].commentText", is("Esimene meeldetuletus.")))
                .andExpect(jsonPath("$[0].student.email", is("i.inaktiivne@taltech.ee")));
    }

    // ======================================================================
    // 3) GET — student belongs to another course → expect empty
    // ======================================================================
    @Test
    void getComments_studentFromOtherCourse_returnsEmpty() throws Exception {

        long studentId = getUserId("g.mittesellelkursuseltaktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/comments",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ======================================================================
    // 4) GET — comments must be sorted by date_time DESC
    // ====================F==================================================
    @Test
    void getComments_sortedDescendingByDate() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        var result = mvc.perform(get("/api/courses/{courseId}/students/{studentId}/comments",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isOk())
                .andReturn();

        var mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        var root = mapper.readTree(result.getResponse().getContentAsString());

        String last = "";
        for (var c : root) {
            String dt = c.get("dateTime").asText();
            if (!last.isEmpty() && dt.compareTo(last) > 0) {
                fail("Comments are not sorted DESC by date_time");
            }
            last = dt;
        }
    }

    // ======================================================================
    // 5) POST — teacher can create a new comment
    // ======================================================================
    @Test
    void addComment_teacherCreatesComment() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        String json = """
                {
                  "studentId": %d,
                  "teacherId": 1,
                  "courseId": %d,
                  "type": "EMAIL",
                  "commentText": "Testing insert"
                }
                """.formatted(studentId, TEST_COURSE_ID);

        mvc.perform(post("/api/courses/{courseId}/students/{studentId}/comments",
                        TEST_COURSE_ID, studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.commentText", is("Testing insert")))
                .andExpect(jsonPath("$.student.id", is((int) studentId)));
    }


    // ----------------------------------------------------------------------
    // Helper: Get user id by email
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

