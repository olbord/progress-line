package ee.taltech.veebirakenduseprojekt.repository;

import ee.taltech.veebirakenduseprojekt.entity.FeedbackResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackResponse, Long> {

    @Query("""
        SELECT fr FROM FeedbackResponse fr
        JOIN FETCH fr.attempt fa
        JOIN FETCH fa.feedback f
        WHERE f.courseId = :courseId
          AND fa.student.id = :studentId
        ORDER BY fa.createdAt DESC, fr.id ASC
    """)
    List<FeedbackResponse> findAllStudentResponsesByCourseAndStudentId(
            @Param("courseId") Long courseId,
            @Param("studentId") Long studentId);
}


