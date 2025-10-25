package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import java.util.*;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByStudentId(Long studentId);

    @Query(value = """
        SELECT *
        FROM comment
        WHERE user_id = :studentId
          AND course_id = :courseId
        """, nativeQuery = true)
    List<Comment> findByStudentIdAndCourseId(@Param("studentId") Long studentId,
                                             @Param("courseId") Long courseId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.student")
    List<Comment> findAllWithStudents();
}
