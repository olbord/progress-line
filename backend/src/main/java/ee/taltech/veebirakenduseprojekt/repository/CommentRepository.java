package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStudent(Student student);

    List<Comment> findByStudent_Id(Long studentId);
}
