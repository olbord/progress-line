package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import java.util.*;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
        SELECT *
        FROM comment
        WHERE user_id = :studentId
          AND course_id = :courseId
          ORDER BY date_time DESC
        """, nativeQuery = true)
    List<Comment> findByStudentIdAndCourseId(@Param("courseId") Long courseId,
                                             @Param("studentId") Long studentId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.student")
    List<Comment> findAllWithStudents();
}
