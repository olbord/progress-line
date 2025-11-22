package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.*;
import jakarta.transaction.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.*;
import org.springframework.test.context.*;
import org.springframework.test.web.servlet.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
class CourseStudentControllerIntegrationTest extends AbstractIntegrationTest {

    private static final long TEST_COURSE_ID = 35065L;

    @Autowired
    private MockMvc mvc;

    // ----------------------------------------------------------------------
    // GET /api/courses/{courseId}/students/{studentId}
    // ----------------------------------------------------------------------
    @Test
    void getStudentOnCourse_whenUserIsTeacher_returnsCorrectStudentDto() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("a.aktiivne@taltech.ee"))
                .andExpect(jsonPath("$.firstName").value("Anna"));
    }

    // ----------------------------------------------------------------------
    // GET /api/courses/{courseId}/students/with-declarations
    // ----------------------------------------------------------------------
    @Test
    void getActiveStudentsWithDeclarations_whenUsersWithAndWithoutDeclarations_returnsOnlyExpectedStudents() throws Exception {

        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        TEST_COURSE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                // Must contain active student with declaration:
                .andExpect(jsonPath("$.content[*].email", hasItem("a.aktiivne@taltech.ee")))
                // Must NOT contain inactive student with declaration
                .andExpect(jsonPath("$.content[*].email", not(hasItem("i.inaktiivne@taltech.ee"))))
                // Must NOT contain student without declaration
                .andExpect(jsonPath("$.content[*].email", not(hasItem("d.deklaratsioonita@taltech.ee"))))
                // Must NOT contain student from other course
                .andExpect(jsonPath("$.content[*].email", not(hasItem("d.mitteselleltkursuselt@taltech.ee"))));
    }

    // ----------------------------------------------------------------------
    // GET /api/courses/{courseId}/students/no-declarations
    // ----------------------------------------------------------------------
    @Test
    void getStudentsWithoutDeclarations_whenUsersWithAndWithoutDeclarations_returnsOnlyCorrectStudent() throws Exception {

        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        TEST_COURSE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[*].email", hasItem("d.deklaratsioonita@taltech.ee")))
                // Should NOT include declared or inactive or from other course
                .andExpect(jsonPath("$.content[*].email", not(hasItem("a.aktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("i.inaktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("e.mittesellelkursusel@taltech.ee"))));
    }

    // ----------------------------------------------------------------------
    // GET /api/courses/{courseId}/students/inactive
    // ----------------------------------------------------------------------
    @Test
    void getInactiveStudents_whenUsersActiveAndInactive_returnsOnlyInactiveStudent() throws Exception {

        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        TEST_COURSE_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[*].email", hasItem("i.inaktiivne@taltech.ee")))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("a.aktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("d.deklaratsioonita@taltech.ee"))));
    }

    // ----------------------------------------------------------------------
    // POST /api/courses/{cid}/students/{sid}/inactive
    // ----------------------------------------------------------------------
    @Test
    void markInactive_whenStudentActive_marksStudentAsInactive() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(post("/api/courses/{cid}/students/{sid}/inactive",
                        TEST_COURSE_ID, studentId))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Student marked as inactive successfully."));
    }

    @Test
    void markInactive_whenStudentNotInCourse_returnsNotFound() throws Exception {

        long studentId = getUserId("e.mittesellelkursusel@taltech.ee");

        mvc.perform(post("/api/courses/{cid}/students/{sid}/inactive",
                        TEST_COURSE_ID, studentId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not")));
    }

    @Test
    void markInactive_whenStudentAlreadyInactive_returnsBadRequest() throws Exception {

        long studentId = getUserId("i.inaktiivne@taltech.ee");

        mvc.perform(post("/api/courses/{cid}/students/{sid}/inactive",
                        TEST_COURSE_ID, studentId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("inactive")));
    }

    // ----------------------------------------------------------------------
    // DELETE /api/courses/{cid}/students/{sid}/active
    // ----------------------------------------------------------------------
    @Test
    void markActive_whenStudentInactive_marksInactiveStudentActive() throws Exception {

        long studentId = getUserId("i.inaktiivne@taltech.ee");

        mvc.perform(delete("/api/courses/{cid}/students/{sid}/active",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isNoContent());
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
}

