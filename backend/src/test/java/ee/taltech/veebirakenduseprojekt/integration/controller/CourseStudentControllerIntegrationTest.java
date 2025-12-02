package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.AbstractIntegrationTestConfig;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("dev")
class CourseStudentControllerIntegrationTest extends AbstractIntegrationTestConfig {

    private static final long TEST_COURSE_ID = 35065L;
    private static final long SECOND_TEST_COURSE_ID = 66066L;

    @Autowired
    private MockMvc mvc;

    @Test
    void getStudentOnCourse_whenUserIsTeacher_returnsCorrectStudentDto() throws Exception {

        long studentId = getUserId("a.aktiivne@taltech.ee");

        mvc.perform(get("/api/courses/{courseId}/students/{studentId}",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("a.aktiivne@taltech.ee"))
                .andExpect(jsonPath("$.firstName").value("Anna"));
    }

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

    @Test
    void markActive_whenStudentInactive_marksInactiveStudentActive() throws Exception {

        long studentId = getUserId("i.inaktiivne@taltech.ee");

        mvc.perform(delete("/api/courses/{cid}/students/{sid}/active",
                        TEST_COURSE_ID, studentId))
                .andExpect(status().isNoContent());
    }

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
    // active
    // ----------------------------------------------------------------------
    @Test
    void getActiveStudentsWithDeclarations_sortedByFirstNameAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "firstName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName", is("Anna")))
                .andExpect(jsonPath("$.content[1].firstName", is("Gea")));
    }

    @Test
    void getActiveStudentsWithDeclarations_sortedByFirstNameDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "firstName,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName", is("Gea")))
                .andExpect(jsonPath("$.content[1].firstName", is("Anna")));
    }

    @Test
    void getActiveStudentsWithDeclarations_sortedByLastNameAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "lastName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName", is("Aktiivne")))
                .andExpect(jsonPath("$.content[1].lastName", is("Teiseltkursuseltaktiivne")));
    }

    @Test
    void getActiveStudentsWithDeclarations_sortedByLastNameDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "lastName,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName", is("Teiseltkursuseltaktiivne")))
                .andExpect(jsonPath("$.content[1].lastName", is("Aktiivne")));
    }

    @Test
    void getActiveStudentsWithDeclarations_sortedByEmailAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "email,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email", is("a.aktiivne@taltech.ee")))
                .andExpect(jsonPath("$.content[1].email", is("g.mittesellelkursuseltaktiivne@taltech.ee")));
    }

    @Test
    void getActiveStudentsWithDeclarations_sortedByEmailDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "email,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email", is("g.mittesellelkursuseltaktiivne@taltech.ee")))
                .andExpect(jsonPath("$.content[1].email", is("a.aktiivne@taltech.ee")));
    }

    @Test
    void getActiveStudentsWithDeclarations_sortedByTotalPointsAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "totalPoints,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].totalPoints", is(0)))
                .andExpect(jsonPath("$.content[1].totalPoints", is(88.4)));
    }

    @Test
    void getActiveStudentsWithDeclarations_sortedByTotalPointsDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "totalPoints,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].totalPoints", is(88.4)))
                .andExpect(jsonPath("$.content[1].totalPoints", is(0)));
    }

    @Test
    void getActiveStudentsWithDeclarations_search() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        TEST_COURSE_ID)
                        .param("search", "Anna Aktiivne a.aktiivne@taltech.ee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].email", is("a.aktiivne@taltech.ee")));
    }

    // ----------------------------------------------------------------------
    // inactive
    // ----------------------------------------------------------------------
    @Test
    void getInactiveStudentsWithDeclarations_sortedByFirstNameAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "firstName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName", is("Inno")))
                .andExpect(jsonPath("$.content[1].firstName", is("Õie")));
    }

    @Test
    void getInactiveStudentsWithDeclarations_sortedByFirstNameDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "firstName,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName", is("Õie")))
                .andExpect(jsonPath("$.content[1].firstName", is("Inno")));
    }

    @Test
    void getInactiveStudentsWithDeclarations_sortedByLastNameAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "lastName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName", is("Inaktiivne")))
                .andExpect(jsonPath("$.content[1].lastName", is("Teiseltkursuseltinaktiivne")));
    }

    @Test
    void getInactiveStudentsWithDeclarations_sortedByLastNameDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "lastName,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName", is("Teiseltkursuseltinaktiivne")))
                .andExpect(jsonPath("$.content[1].lastName", is("Inaktiivne")));
    }

    @Test
    void getInactiveStudentsWithDeclarations_sortedByEmailAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "email,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email", is("i.inaktiivne@taltech.ee")))
                .andExpect(jsonPath("$.content[1].email", is("õ.mittesellelkursuseltinaktiivne@taltech.ee")));
    }

    @Test
    void getInactiveStudentsWithDeclarations_sortedByEmailDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "email,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email", is("õ.mittesellelkursuseltinaktiivne@taltech.ee")))
                .andExpect(jsonPath("$.content[1].email", is("i.inaktiivne@taltech.ee")));
    }

    @Test
    void getInactiveStudentsWithDeclarations_sortedByTotalPointsAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "totalPoints,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].totalPoints", is(0)))
                .andExpect(jsonPath("$.content[1].totalPoints", is(85.52)));
    }

    @Test
    void getInactiveStudentsWithDeclarations_sortedByTotalPointsDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "totalPoints,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].totalPoints", is(85.52)))
                .andExpect(jsonPath("$.content[1].totalPoints", is(0)));
    }

    @Test
    void getInactiveStudentsWithDeclarations_search() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        TEST_COURSE_ID)
                        .param("search", "Inno Inaktiivne i.inaktiivne@taltech.ee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].email", is("i.inaktiivne@taltech.ee")));
    }

    // ----------------------------------------------------------------------
    // no-declarations
    // ----------------------------------------------------------------------
    @Test
    void getStudentsWithoutDeclarations_sortedByFirstNameAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "firstName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName", is("Cleo")))
                .andExpect(jsonPath("$.content[1].firstName", is("Dagmar")))
                .andExpect(jsonPath("$.content[2].firstName", is("Eno")))
                .andExpect(jsonPath("$.content[3].firstName", is("Hardo")));
    }

    @Test
    void getStudentsWithoutDeclarations_sortedByFirstNameDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "firstName,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName", is("Hardo")))
                .andExpect(jsonPath("$.content[1].firstName", is("Eno")))
                .andExpect(jsonPath("$.content[2].firstName", is("Dagmar")))
                .andExpect(jsonPath("$.content[3].firstName", is("Cleo")));
    }

    @Test
    void getStudentsWithoutDeclarations_sortedByLastNameAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "lastName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName", is("Deklaratsioonita")))
                .andExpect(jsonPath("$.content[1].lastName", is("Inaktiivnedeklaratsioonita")))
                .andExpect(jsonPath("$.content[2].lastName", is("Teiseltkursuseltdeklaratsioonita")))
                .andExpect(jsonPath("$.content[3].lastName", is("Teiseltkursuseltinaktiivneeklaratsioonita")));
    }

    @Test
    void getStudentsWithoutDeclarations_sortedByLastNameDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "lastName,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].lastName", is("Teiseltkursuseltinaktiivneeklaratsioonita")))
                .andExpect(jsonPath("$.content[1].lastName", is("Teiseltkursuseltdeklaratsioonita")))
                .andExpect(jsonPath("$.content[2].lastName", is("Inaktiivnedeklaratsioonita")))
                .andExpect(jsonPath("$.content[3].lastName", is("Deklaratsioonita")));
    }

    @Test
    void getStudentsWithoutDeclarations_sortedByEmailAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "email,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email", is("c.inaktiivnedeklaratsioonita@taltech.ee")))
                .andExpect(jsonPath("$.content[1].email", is("d.deklaratsioonita@taltech.ee")))
                .andExpect(jsonPath("$.content[2].email", is("e.mittesellelkursuseltdeklaratsioonita@taltech.ee")))
                .andExpect(jsonPath("$.content[3].email", is("h.mittesellelkursuseltinaktiivneeklaratsioonita@taltech.ee")));
    }

    @Test
    void getStudentsWithoutDeclarations_sortedByEmailDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "email,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].email", is("h.mittesellelkursuseltinaktiivneeklaratsioonita@taltech.ee")))
                .andExpect(jsonPath("$.content[1].email", is("e.mittesellelkursuseltdeklaratsioonita@taltech.ee")))
                .andExpect(jsonPath("$.content[2].email", is("d.deklaratsioonita@taltech.ee")))
                .andExpect(jsonPath("$.content[3].email", is("c.inaktiivnedeklaratsioonita@taltech.ee")));
    }

    @Test
    void getStudentsWithoutDeclarations_sortedByTotalPointsAsc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "totalPoints,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].totalPoints", is(0)))
                .andExpect(jsonPath("$.content[1].totalPoints", is(0)))
                .andExpect(jsonPath("$.content[2].totalPoints", is(0)))
                .andExpect(jsonPath("$.content[3].totalPoints", is(76.42)));
    }

    @Test
    void getStudentsWithoutDeclarations_sortedByTotalPointsDesc() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        SECOND_TEST_COURSE_ID)
                        .param("sort", "totalPoints,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].totalPoints", is(76.42)))
                .andExpect(jsonPath("$.content[1].totalPoints", is(0)))
                .andExpect(jsonPath("$.content[2].totalPoints", is(0)))
                .andExpect(jsonPath("$.content[3].totalPoints", is(0)));
    }

    @Test
    void getStudentsWithoutDeclarations_search() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        TEST_COURSE_ID)
                        .param("search", "Dagmar Deklaratsioonita d.deklaratsioonita@taltech.ee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].email", is("d.deklaratsioonita@taltech.ee")));
    }

    // ----------------------------------------------------------------------
    // Pagination
    // ----------------------------------------------------------------------
    @Test
    void activeStudentsWithDeclarationsPaginationWorks() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/with-declarations",
                        TEST_COURSE_ID)
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)));
    }

    @Test
    void inactiveStudentsWithDeclarationsPaginationWorks() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/inactive",
                        TEST_COURSE_ID)
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)));
    }

    @Test
    void withoutDeclarationStudentPaginationWorks() throws Exception {
        mvc.perform(get("/api/courses/{courseId}/students/no-declarations",
                        TEST_COURSE_ID)
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.totalPages", is(2)));
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

