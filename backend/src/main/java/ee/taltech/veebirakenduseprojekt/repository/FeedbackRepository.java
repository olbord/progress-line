package ee.taltech.veebirakenduseprojekt.repository;

import ee.taltech.veebirakenduseprojekt.entity.FeedbackResponse;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackResponse, Long> {

    @Query(value = """
        SELECT fr.*
        FROM feedback_response fr
        JOIN feedback_attempt fa ON fr.attempt_id = fa.id
        JOIN feedback f ON fa.feedback_id = f.id
        WHERE f.course_id = :courseId
          AND fa.user_id = :studentId
        ORDER BY fa.created_at DESC, fr.id ASC 
    """, nativeQuery = true)
    List<FeedbackResponse> findAllStudentResponsesByCourseAndStudentId(
            @Param("courseId") Long courseId,
            @Param("studentId") Long studentId);
}


