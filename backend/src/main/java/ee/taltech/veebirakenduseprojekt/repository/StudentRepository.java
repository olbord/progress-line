package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = """
        SELECT * FROM "user" u
        WHERE u.id NOT IN (SELECT d.user_id FROM declaration d WHERE course_id = 35065)
        AND u.id IN (SELECT r.user_id FROM user_role r WHERE course_id = 35065 AND role_id = 5)
        """, nativeQuery = true)
    List<Student> findAllWithoutDeclarations();

    @Query(value = """
        SELECT u.*
        FROM "user" u
        JOIN user_role r ON u.id = r.user_id
        JOIN declaration d ON u.id = d.user_id
        WHERE r.course_id = 35065
          AND r.role_id = 5
          AND d.course_id = 35065
        """, nativeQuery = true)
    List<Student> findStudentsWithDeclaration();

    @Query(value = """
        SELECT u.*
        FROM "user" u
        JOIN user_role r ON u.id = r.user_id
        JOIN declaration d ON u.id = d.user_id
        WHERE r.course_id = 35065
          AND r.role_id = 5
          AND d.course_id = 35065
          AND u.id = :studentId
        """, nativeQuery = true)
    Optional<Student> findWithDeclarationById(@Param("studentId") Long studentId);

    @Query(value = """
    SELECT u.*
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id
    JOIN inactive i ON u.id = i.user_id
    WHERE r.course_id = 35065
      AND r.role_id = 5
    """, nativeQuery = true)
    List<Student> findAllInactive();
}
