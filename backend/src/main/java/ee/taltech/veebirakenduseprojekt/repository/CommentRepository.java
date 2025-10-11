package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;
import java.util.*;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStudentId(Long studentId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.student")
    List<Comment> findAllWithStudents();
}
