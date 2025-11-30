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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
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
                // Must NOT contain students without declaration
                .andExpect(jsonPath("$.content[*].email", not(hasItem("d.deklaratsioonita@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("c.inaktiivnedeklaratsioonita@taltech.ee"))))
                // Must NOT contain students from other course
                .andExpect(jsonPath("$.content[*].email", not(hasItem("g.mittesellelkursuseltaktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("e.mittesellelkursuseltdeklaratsioonita@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("õ.mittesellelkursuseltinaktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("h.mittesellelkursuseltinaktiivneeklaratsioonita@taltech.ee"))))
                // Must NOT contain student without course
                .andExpect(jsonPath("$.content[*].email", not(hasItem("r.lihtsaltkasutaja@taltech.ee"))))
                // Must NOT contain student duplicates
                .andExpect(result -> {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(result.getResponse().getContentAsString());
                JsonNode content = root.path("content");

                List<String> emails = new ArrayList<>();
                for (JsonNode node : content) {
                    emails.add(node.get("email").asText());
                }

                long uniqueCount = emails.stream().distinct().count();
                if (emails.size() != uniqueCount) {
                    fail("Duplicate students returned: " + emails);
                }
        });
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
                .andExpect(jsonPath("$.content[*].email", hasItem("c.inaktiivnedeklaratsioonita@taltech.ee")))
                // Should NOT include declared or inactive
                .andExpect(jsonPath("$.content[*].email", not(hasItem("a.aktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("i.inaktiivne@taltech.ee"))))
                // Must NOT contain students from other course
                .andExpect(jsonPath("$.content[*].email", not(hasItem("g.mittesellelkursuseltaktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("e.mittesellelkursuseltdeklaratsioonita@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("õ.mittesellelkursuseltinaktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("h.mittesellelkursuseltinaktiivneeklaratsioonita@taltech.ee"))))
                // Must NOT contain student without course
                .andExpect(jsonPath("$.content[*].email", not(hasItem("r.lihtsaltkasutaja@taltech.ee"))))
                // Must NOT contain student duplicates
                .andExpect(result -> {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(result.getResponse().getContentAsString());
                    JsonNode content = root.path("content");

                    List<String> emails = new ArrayList<>();
                    for (JsonNode node : content) {
                        emails.add(node.get("email").asText());
                    }

                    long uniqueCount = emails.stream().distinct().count();
                    if (emails.size() != uniqueCount) {
                        fail("Duplicate students returned: " + emails);
                    }
                });
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
                .andExpect(jsonPath("$.content[*].email", not(hasItem("c.inaktiivnedeklaratsioonita@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("a.aktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("d.deklaratsioonita@taltech.ee"))))
                // Must NOT contain students from other course
                .andExpect(jsonPath("$.content[*].email", not(hasItem("g.mittesellelkursuseltaktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("e.mittesellelkursuseltdeklaratsioonita@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("õ.mittesellelkursuseltinaktiivne@taltech.ee"))))
                .andExpect(jsonPath("$.content[*].email", not(hasItem("h.mittesellelkursuseltinaktiivneeklaratsioonita@taltech.ee"))))
                // Must NOT contain student without course
                .andExpect(jsonPath("$.content[*].email", not(hasItem("r.lihtsaltkasutaja@taltech.ee"))))
                // Must NOT contain student duplicates
                .andExpect(result -> {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(result.getResponse().getContentAsString());
                    JsonNode content = root.path("content");

                    List<String> emails = new ArrayList<>();
                    for (JsonNode node : content) {
                        emails.add(node.get("email").asText());
                    }

                    long uniqueCount = emails.stream().distinct().count();
                    if (emails.size() != uniqueCount) {
                        fail("Duplicate students returned: " + emails);
                    }
                });
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

        long studentId = getUserId("e.mittesellelkursuseltdeklaratsioonita@taltech.ee");

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
    // GET /api/courses/{courseId}/students/{studentId}/status
    // ----------------------------------------------------------------------
    @Test
    void getStudentStatus_whenUsersActiveWithDeclaration_returnsActiveTrueDeclaredTrue() throws Exception {
        long activeWithDeclarationStudentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{cid}/students/{sid}/status",
                        TEST_COURSE_ID, activeWithDeclarationStudentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(activeWithDeclarationStudentId))
                .andExpect(jsonPath("$.courseId").value(TEST_COURSE_ID))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.declared").value(true));
    }

    @Test
    void getStudentStatus_whenUsersActiveWithoutDeclaration_returnsActiveTrueDeclaredFalse() throws Exception {
        long activeWithoutDeclarationStudentId = getUserId("d.deklaratsioonita@taltech.ee");

        mvc.perform(get("/api/courses/{cid}/students/{sid}/status",
                        TEST_COURSE_ID, activeWithoutDeclarationStudentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(activeWithoutDeclarationStudentId))
                .andExpect(jsonPath("$.courseId").value(TEST_COURSE_ID))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.declared").value(false));
    }

    @Test
    void getStudentStatus_whenUsersInactiveWithDeclaration_returnsActiveFalseDeclaredTrue() throws Exception {
        long inactiveWithDeclarationStudentId = getUserId("i.inaktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{cid}/students/{sid}/status",
                        TEST_COURSE_ID, inactiveWithDeclarationStudentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(inactiveWithDeclarationStudentId))
                .andExpect(jsonPath("$.courseId").value(TEST_COURSE_ID))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.declared").value(true));
    }

    @Test
    void getStudentStatus_whenUsersInactiveWithoutDeclaration_returnsActiveFalseDeclaredFalse() throws Exception {
        long inactiveWithoutDeclarationStudentId = getUserId("c.inaktiivnedeklaratsioonita@taltech.ee");

        mvc.perform(get("/api/courses/{cid}/students/{sid}/status",
                        TEST_COURSE_ID, inactiveWithoutDeclarationStudentId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value(inactiveWithoutDeclarationStudentId))
                .andExpect(jsonPath("$.courseId").value(TEST_COURSE_ID))
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.declared").value(false));
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

