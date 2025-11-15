package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<User, Long> {

    @Query(value = """
        SELECT u.*
        FROM "user" u
        JOIN user_role r ON u.id = r.user_id
        JOIN declaration d ON u.id = d.user_id
        WHERE r.course_id = :courseId
          AND r.role_id = 5
          AND d.course_id = :courseId
          AND u.id = :studentId
        """, nativeQuery = true)
    Optional<User> findWithDeclarationByCourseAndId(@Param("courseId") Long courseId,
                                                       @Param("studentId") Long studentId);

    @Query(
            value = """
            SELECT u.*
            FROM "user" u
            JOIN user_role r ON u.id = r.user_id
            JOIN declaration d ON u.id = d.user_id
            WHERE r.course_id = :courseId
              AND r.role_id = 5
              AND d.course_id = :courseId
              AND u.id NOT IN (SELECT i.user_id FROM inactive i)
            """, countQuery = """
            SELECT COUNT(*)
            FROM "user" u
            JOIN user_role r ON u.id = r.user_id
            JOIN declaration d ON u.id = d.user_id
            WHERE r.course_id = :courseId
              AND r.role_id = 5
              AND d.course_id = :courseId
              AND u.id NOT IN (SELECT i.user_id FROM inactive i)
            """, nativeQuery = true
    )
    Page<User> findPaginatedActiveWithDeclarationByCourse(
            @Param("courseId") Long courseId,
            Pageable pageable);

    @Query(
            value = """
        SELECT u.*
        FROM "user" u
        JOIN user_role r ON u.id = r.user_id
        WHERE r.course_id = :courseId
          AND r.role_id = 5
          AND u.id NOT IN (
              SELECT d.user_id
              FROM declaration d
              WHERE d.course_id = :courseId
          )
        """, countQuery = """
        SELECT COUNT(*)
        FROM "user" u
        JOIN user_role r ON u.id = r.user_id
        WHERE r.course_id = :courseId
          AND r.role_id = 5
          AND u.id NOT IN (
              SELECT d.user_id
              FROM declaration d
              WHERE d.course_id = :courseId
          )
        """, nativeQuery = true
    )
    Page<User> findPaginatedWithoutDeclarationByCourse(@Param("courseId") Long courseId,
                                                       Pageable pageable);

    @Query(
            value = """
        SELECT u.*
        FROM "user" u
        JOIN user_role r ON u.id = r.user_id
        JOIN declaration d ON u.id = d.user_id
        JOIN inactive i ON u.id = i.user_id
        WHERE r.course_id = :courseId
          AND r.role_id = 5
          AND d.course_id = :courseId
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM "user" u
        JOIN user_role r ON u.id = r.user_id
        JOIN declaration d ON u.id = d.user_id
        JOIN inactive i ON u.id = i.user_id
        WHERE r.course_id = :courseId
          AND r.role_id = 5
          AND d.course_id = :courseId
        """, nativeQuery = true
    )
    Page<User> findPaginatedInactiveWithDeclarationByCourse(
            @Param("courseId") Long courseId,
            Pageable pageable
    );
}
