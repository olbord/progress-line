package ee.taltech.veebirakenduseprojekt.repository;

import ee.taltech.veebirakenduseprojekt.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("""
        SELECT c FROM Course c
        JOIN c.userRoles r
        WHERE r.user.email = :teacherEmail
          AND r.roleId = 3
          AND (:query IS NULL OR :query = ''
               OR LOWER(c.shortName) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%')))
        ORDER BY c.fullName ASC
    """)
    List<Course> searchCoursesByEmail(@Param("teacherEmail") String teacherEmail, @Param("query") String query);
}
