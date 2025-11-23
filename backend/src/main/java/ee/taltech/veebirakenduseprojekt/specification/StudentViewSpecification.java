package ee.taltech.veebirakenduseprojekt.specification;

import ee.taltech.veebirakenduseprojekt.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class StudentViewSpecification {

    public static Specification<User> search(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) {
                return cb.conjunction();
            }

            String like = "%" + term.toLowerCase() + "%";

            var concatAll = cb.lower(
                    cb.concat(
                            cb.concat(
                                    cb.concat(root.get("firstName"), " "),
                                    cb.concat(root.get("lastName"), " ")
                            ),
                            root.get("email")
                    )
            );

            return cb.like(concatAll, like);
        };
    }


    public static Specification<User> activeStudents(Long courseId) {
        return (root, query, cb) -> {
            Join<User, UserRole> role = root.join("roles");
            Join<User, Declaration> dec = root.join("declarations");

            Subquery<Long> inactiveSub = query.subquery(Long.class);
            Root<Inactive> inactiveRoot = inactiveSub.from(Inactive.class);
            inactiveSub.select(inactiveRoot.get("student").get("id"))
                    .where(
                            cb.equal(inactiveRoot.get("course").get("id"), courseId),
                            cb.equal(inactiveRoot.get("student").get("id"), root.get("id"))
                    );

            return cb.and(
                    cb.equal(role.get("course").get("id"), courseId),
                    cb.equal(role.get("roleId"), 5),
                    cb.equal(dec.get("course").get("id"), courseId),
                    cb.not(cb.exists(inactiveSub))
            );
        };
    }
}

