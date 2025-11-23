package ee.taltech.veebirakenduseprojekt.repository;

import ee.taltech.veebirakenduseprojekt.entity.StudentView;
import ee.taltech.veebirakenduseprojekt.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

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
    SELECT DISTINCT
      u.id AS user_id,
      :courseId AS course_id,
      u.first_name,
      u.last_name,
      u.email,
      v.total_points
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id AND r.course_id = :courseId
    JOIN declaration d ON u.id = d.user_id AND d.course_id = :courseId
    LEFT JOIN student_view v
            ON v.user_id = u.id AND v.course_id = :courseId
    WHERE r.course_id = :courseId
      AND r.role_id = 5
      AND d.course_id = :courseId
      AND u.id NOT IN (SELECT i.user_id FROM inactive i WHERE u.id= i.user_id AND i.course_id = :courseId)
    ORDER BY u.first_name ASC
    """, countQuery = """
    SELECT COUNT(DISTINCT u.id)
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id AND r.course_id = :courseId
    JOIN declaration d ON u.id = d.user_id AND d.course_id = :courseId
    WHERE r.course_id = :courseId
      AND r.role_id = 5
      AND d.course_id = :courseId
      AND u.id NOT IN (SELECT i.user_id FROM inactive i WHERE u.id = i.user_id AND i.course_id = :courseId)
    """, nativeQuery = true
    )
    Page<StudentView> findPaginatedActiveWithDeclarationByCourse(
            @Param("courseId") Long courseId,
            Pageable pageable);

    @Query(
            value = """
    SELECT DISTINCT
      u.id AS user_id,
      :courseId AS course_id,
      u.first_name,
      u.last_name,
      u.email,
      v.total_points
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id AND r.course_id = :courseId
    LEFT JOIN student_view v
            ON v.user_id = u.id AND v.course_id = :courseId
    WHERE r.course_id = :courseId
      AND r.role_id = 5
      AND u.id NOT IN (
          SELECT d.user_id
          FROM declaration d
          WHERE u.id = d.user_id AND d.course_id = :courseId)
    ORDER BY u.first_name ASC
    """, countQuery = """
    SELECT COUNT(DISTINCT u.id)
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id AND r.course_id = :courseId
    WHERE r.course_id = :courseId
      AND r.role_id = 5
      AND u.id NOT IN (
          SELECT d.user_id
          FROM declaration d
          WHERE u.id = d.user_id AND d.course_id = :courseId)
    """, nativeQuery = true
    )
    Page<StudentView> findPaginatedWithoutDeclarationByCourse(@Param("courseId") Long courseId,
                                                       Pageable pageable);

    @Query(
            value = """
    SELECT DISTINCT
      u.id AS user_id,
      :courseId AS course_id,
      u.first_name,
      u.last_name,
      u.email,
      v.total_points
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id AND r.course_id = :courseId
    JOIN declaration d ON u.id = d.user_id AND d.course_id = :courseId
    JOIN inactive i ON u.id = i.user_id AND i.course_id = :courseId
    LEFT JOIN student_view v
            ON v.user_id = u.id AND v.course_id = :courseId
    WHERE r.course_id = :courseId
      AND r.role_id = 5
      AND d.course_id = :courseId
    ORDER BY u.first_name ASC
    """, countQuery = """
    SELECT COUNT(DISTINCT u.id)
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id AND r.course_id = :courseId
    JOIN declaration d ON u.id = d.user_id AND d.course_id = :courseId
    JOIN inactive i ON u.id = i.user_id AND i.course_id = :courseId
    WHERE r.course_id = :courseId
      AND r.role_id = 5
      AND d.course_id = :courseId
    """, nativeQuery = true
    )
    Page<StudentView> findPaginatedInactiveWithDeclarationByCourse(
            @Param("courseId") Long courseId,
            Pageable pageable
    );

    @Query(value = """
    SELECT u.*
    FROM "user" u
    JOIN user_role r ON u.id = r.user_id
    JOIN inactive i ON u.id = i.user_id
    WHERE r.course_id = :courseId
      AND r.role_id = 5
      AND i.course_id = :courseId
      AND u.id = :studentId
    """, nativeQuery = true)
    Optional<User> findInactiveStudentByCourseAndId(@Param("courseId") Long courseId,
                                                    @Param("studentId") Long studentId);
}
