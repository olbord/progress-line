package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeclarationRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM declaration d WHERE d.user_id = :studentId AND d.course_id = :courseId", nativeQuery = true)
    void deleteDeclarationByStudentIdAndCourseId(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
