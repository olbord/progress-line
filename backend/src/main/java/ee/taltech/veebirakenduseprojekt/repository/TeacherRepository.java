package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<User, Long> {

    @Query(value = """
    SELECT u.*
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id
    WHERE r.course_id = :courseId
      AND r.role_id = 3
    """, nativeQuery = true)
    List<User> getTeachersByCourse(@Param("courseId") Long courseId);

    @Query(value = """
    SELECT EXISTS (
        SELECT 1
        FROM "user" u
        JOIN user_role r ON u.id = r.user_id
        WHERE u.email = :email
          AND r.course_id = :courseId
          AND r.role_id = 3
    )
    """, nativeQuery = true)
    boolean isTeacherOfCourse(@Param("email") String email, @Param("courseId") Long courseId);

    User findByEmail(String email);
}
