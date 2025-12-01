package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.AbstractIntegrationTest;
import ee.taltech.veebirakenduseprojekt.service.CourseService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
class EmailControllerIntegrationTest extends AbstractIntegrationTest {

    private static final long TEST_COURSE_ID = 35065L;

    @Autowired
    private MockMvc mvc;

    // Mock ONLY CourseService — so that there is no actual email sending
    @MockBean
    private CourseService courseService;


    // ======================================================================
    // 1) Invalid DTO → @Valid fails → MethodArgumentNotValidException → 500
    // ======================================================================
    @Test
    void sendEmail_invalidRequest_returns500() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(post("/api/courses/{courseId}/students/{studentId}/send-email",
                        TEST_COURSE_ID, studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))           // emailBody missing
                .andExpect(status().isInternalServerError());
    }


    // ======================================================================
    // 2) Teacher sends valid email → OK + service invoked correctly
    // ======================================================================
    @Test
    void sendEmail_teacherSendsEmail_success() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        Mockito.doNothing()
                .when(courseService)
                .sendAndSaveEmail(anyLong(), anyLong(), anyString(), anyString());

        mvc.perform(post("/api/courses/{courseId}/students/{studentId}/send-email",
                        TEST_COURSE_ID, studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"emailBody": "Hello student!"}
                                """))
                .andExpect(status().isOk());

        verify(courseService, times(1))
                .sendAndSaveEmail(
                        TEST_COURSE_ID,
                        studentId,
                        "Hello student!",
                        "kadi.jairus@taltech.ee"
                );
    }


    // ======================================================================
    // 3) Student belongs to another course → controller allows → 200 OK
    // ======================================================================
    @Test
    void sendEmail_studentNotInCourse_stillOk() throws Exception {

        long otherStudentId = getUserId("g.mittesellelkursuseltaktiivne@taltech.ee");

        Mockito.doNothing()
                .when(courseService)
                .sendAndSaveEmail(anyLong(), anyLong(), anyString(), anyString());

        mvc.perform(post("/api/courses/{courseId}/students/{studentId}/send-email",
                        TEST_COURSE_ID, otherStudentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"emailBody": "Cross-course test"}
                                """))
                .andExpect(status().isOk());

        verify(courseService, times(1))
                .sendAndSaveEmail(
                        TEST_COURSE_ID,
                        otherStudentId,
                        "Cross-course test",
                        "kadi.jairus@taltech.ee"
                );
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
