package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = """
        SELECT * FROM "user" u
        WHERE u.id NOT IN (SELECT d.user_id FROM declaration d WHERE course_id = 35065)
        AND u.id IN (SELECT r.user_id FROM user_role r WHERE course_id = 35065 AND role_id = 5)
        """, nativeQuery = true)
    List<Student> findAllWithoutDeclarations();

}