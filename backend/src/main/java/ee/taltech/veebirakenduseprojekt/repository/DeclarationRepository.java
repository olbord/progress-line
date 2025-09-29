package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DeclarationRepository extends JpaRepository<Student, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM declaration d WHERE d.user_id = :studentId", nativeQuery = true)
    void deleteDeclarationByStudentId(@Param("studentId") Long studentId);
}