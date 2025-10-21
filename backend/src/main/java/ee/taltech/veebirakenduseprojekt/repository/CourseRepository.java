package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Currently only the inherited existsById() is used
}
