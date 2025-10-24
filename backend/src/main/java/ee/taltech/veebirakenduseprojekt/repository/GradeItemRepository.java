package ee.taltech.veebirakenduseprojekt.repository;

import ee.taltech.veebirakenduseprojekt.controller.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeItemRepository extends JpaRepository<GradeItem, Long> {
    List<GradeItem> findAllByCourseIdAndUserId(Long courseId, Long userId);

    @Query(value = "SELECT g.*, u.first_name, u.last_name FROM grade_item g " +
            "LEFT JOIN \"user\" u ON u.id = g.user_id " +
            "WHERE g.course_id = :courseId " +
            "AND g.item_type = 'mod' " +
            "AND g.item_name NOT LIKE '% - Style' " +
            "AND g.item_name NOT LIKE '% - Defense' " +
            "AND g.item_name NOT LIKE '% - Defence' " +
            "ORDER BY g.user_id, g.created_at", nativeQuery = true)
    List<GradeItem> findProgressLineDataByCourseId(Long courseId);
}
