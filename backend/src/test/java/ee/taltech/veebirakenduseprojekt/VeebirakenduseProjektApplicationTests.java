package ee.taltech.veebirakenduseprojekt;

import ee.taltech.veebirakenduseprojekt.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class VeebirakenduseProjektApplicationTests {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void repositoryIsNotNull() {
        assertThat(commentRepository).isNotNull();
    }

}
