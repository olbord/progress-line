package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeItemRepository extends JpaRepository<GradeItem, Long> {
    List<GradeItem> findAllByStudentId(Long studentId);
}
