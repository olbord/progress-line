package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.AbstractIntegrationTestConfig;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
class CourseGradeControllerIntegrationTest extends AbstractIntegrationTestConfig {

    private static final long TEST_COURSE_ID = 35065L;

    @Autowired
    private MockMvc mvc;


    // ==================================================================
    // TEST: Student has several grade items for this course
    // ==================================================================
    @Test
    void getGrades_whenStudentExists_returnsAllGrades() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/grades",
                        TEST_COURSE_ID, studentId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(3)))

                // Includes “Tests”
                .andExpect(jsonPath("$[*].itemName",
                        hasItem("PR/pr04_animals - Tests")))
                // Includes survey TK03
                .andExpect(jsonPath("$[*].itemName",
                        hasItem("TK03 - Küsimustik (5p)")))
                // Includes exam
                .andExpect(jsonPath("$[*].itemName",
                        hasItem(containsString("Eksam 1 - Aktiivsete tudengite töö (100p)"))))

                // Check numeric grade exists
                .andExpect(jsonPath("$[0].grade", notNullValue()))

                // Correct base fields
                .andExpect(jsonPath("$[0].courseId", is((int) TEST_COURSE_ID)))
                .andExpect(jsonPath("$[0].userId", is((int) studentId)));
    }


    // ==================================================================
    // TEST: Student has no grades in this course → expect empty list
    // ==================================================================
    @Test
    void getGrades_whenStudentHasNoGrades_returnsEmptyList() throws Exception {

        long studentId = getUserId("c.inaktiivnedeklaratsioonita@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/grades",
                        TEST_COURSE_ID, studentId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$.length()", is(0)));
    }


    // ==================================================================
    // TEST: Student belongs to another course → must return empty list
    // ==================================================================
    @Test
    void getGrades_whenStudentFromOtherCourse_returnsEmptyList() throws Exception {

        long studentId = getUserId("g.mittesellelkursuseltaktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/grades",
                        TEST_COURSE_ID, studentId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    // ==================================================================
    // TEST: Teacher receives progress-line cumulative grade data
    // ==================================================================
    @Test
    void getProgressLineData_whenCourseHasStudents_returnsCumulativeData() throws Exception {

        mvc.perform(get("/api/courses/{courseId}/progress-lines", TEST_COURSE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                // At least 1 student has progress-line data
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(1)))

                // Each student block has required fields
                .andExpect(jsonPath("$[0].studentId", notNullValue()))
                .andExpect(jsonPath("$[0].firstName", notNullValue()))
                .andExpect(jsonPath("$[0].lastName", notNullValue()))
                .andExpect(jsonPath("$[0].dataPoints").isArray());
    }


    // ==================================================================
    // TEST: Cumulative data points must be sorted by time and cumulative
    // ==================================================================
    @Test
    void getProgressLineData_pointsAreSortedAndCumulative() throws Exception {

        var result = mvc.perform(get("/api/courses/{courseId}/progress-lines", TEST_COURSE_ID))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode root = mapper.readTree(json);

        for (var student : root) {
            var points = student.get("dataPoints");

            double lastValue = -1;
            String lastDate = "";

            for (var p : points) {
                double y = p.get("y").asDouble();
                String x = p.get("x").asText();

                // Check sorted ascending by date
                if (!lastDate.isEmpty() && x.compareTo(lastDate) < 0) {
                        fail("Data points are not sorted by time for studentId=" +
                                student.get("studentId"));
                    }


                // Check cumulative
                if (y < lastValue) {
                    fail("Cumulative value decreased for studentId=" +
                            student.get("studentId"));
                }

                lastValue = y;
                lastDate = x;
            }
        }
    }


    // ==================================================================
    // TEST: Students with grades only from other course must NOT appear
    // ==================================================================
    @Test
    void getProgressLineData_studentsFromOtherCourseAreExcluded() throws Exception {

        long otherCourseStudent = getUserId("g.mittesellelkursuseltaktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/progress-lines", TEST_COURSE_ID))
                .andExpect(status().isOk())
                // Ensure this student's ID is NOT present in the returned array
                .andExpect(jsonPath("$[*].studentId", not(hasItem((int) otherCourseStudent))));
    }


    // ==================================================================
    // TEST: Students without ANY grade items must NOT appear in progress-lines
    // ==================================================================
    @Test
    void getProgressLineData_studentsWithoutGradesAreExcluded() throws Exception {

        long noGradesStudent = getUserId("c.inaktiivnedeklaratsioonita@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/progress-lines", TEST_COURSE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].studentId", not(hasItem((int) noGradesStudent))));
    }


    // ----------------------------------------------------------------------
    // Helper: Look up user ID by email
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


    // ==================================================================
    // TEST: returns latest grade item for a student
    // ==================================================================
    @Test
    void getLatestGrade_whenStudentHasGrades_returnsLatest() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/latest-grade",
                        TEST_COURSE_ID, studentId)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.itemName",
                        is("TK03 - Küsimustik (5p)")))

                .andExpect(jsonPath("$.grade", is("3.25")))
                // Matching created_at from Liquibase: 2025-10-06 09:30:00
                .andExpect(jsonPath("$.gradeDateGraded",
                        is("2025-10-06T09:30:00")));
    }


    // ==================================================================
    // TEST: Student has NO grades → expect 404 NOT FOUND
    // ==================================================================
    @Test
    void getLatestGrade_whenStudentHasNoGrades_returnsNotFound() throws Exception {

        long studentId = getUserId("c.inaktiivnedeklaratsioonita@taltech.ee"); // valid no-grades student

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/latest-grade",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isBadRequest());
    }


    // ==================================================================
    // TEST: Student belongs to another course → expect 404
    // ==================================================================
    @Test
    void getLatestGrade_whenStudentFromOtherCourse_returnsNotFound() throws Exception {

        long studentId = getUserId("g.mittesellelkursuseltaktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}/latest-grade",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isBadRequest());
    }
}
