package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InactiveRepository extends JpaRepository<Inactive, Long> {
    boolean existsByCourseIdAndStudentId(Long courseId, Long studentId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM inactive i WHERE i.user_id = :studentId AND i.course_id = :courseId", nativeQuery = true)
    void deleteInactiveByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
