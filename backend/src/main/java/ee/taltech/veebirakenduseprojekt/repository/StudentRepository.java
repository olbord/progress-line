package ee.taltech.veebirakenduseprojekt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = """
        SELECT * FROM "user" u
        WHERE u.id NOT IN (SELECT n.user_id FROM no_declaration n)
        """, nativeQuery = true)
    List<Student> findAllWithoutDeclarations();

}