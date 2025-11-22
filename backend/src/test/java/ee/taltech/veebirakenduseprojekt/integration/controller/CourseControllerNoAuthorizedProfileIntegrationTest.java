package ee.taltech.veebirakenduseprojekt.integration.controller;

import ee.taltech.veebirakenduseprojekt.integration.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import jakarta.transaction.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.web.servlet.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CourseControllerNoAuthorizedProfileIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private MockMvc mvc;

    @Test
    void getCourses_whenUserIsTeacher_returnsIsUnauthorized() throws Exception {
        mvc.perform(get("/api/courses"))
                .andExpect(status().isUnauthorized());
    }

}
