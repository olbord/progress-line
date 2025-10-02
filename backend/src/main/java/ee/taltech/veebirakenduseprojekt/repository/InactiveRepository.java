package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InactiveRepository extends JpaRepository<Inactive, Long> {
    boolean existsByStudentId(Long studentId);
}
