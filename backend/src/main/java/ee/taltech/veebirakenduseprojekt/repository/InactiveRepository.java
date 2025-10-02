package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

@Repository
public interface InactiveRepository extends JpaRepository<Inactive, Long> {
    boolean existsByStudentId(Long studentId);
}
