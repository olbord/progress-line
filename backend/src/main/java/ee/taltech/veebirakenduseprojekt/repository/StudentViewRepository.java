package ee.taltech.veebirakenduseprojekt.repository;

import ee.taltech.veebirakenduseprojekt.entity.StudentCourseId;
import ee.taltech.veebirakenduseprojekt.entity.StudentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StudentViewRepository extends JpaRepository<StudentView, StudentCourseId>,
        JpaSpecificationExecutor<StudentView> {
    Page<StudentView> findByIdUserIdInAndIdCourseId(
            List<Long> userIds,
            Long courseId,
            Pageable pageable
    );

}
