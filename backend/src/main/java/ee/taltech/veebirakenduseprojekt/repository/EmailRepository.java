package ee.taltech.veebirakenduseprojekt.repository;

import ee.taltech.veebirakenduseprojekt.entity.Email;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {

}
