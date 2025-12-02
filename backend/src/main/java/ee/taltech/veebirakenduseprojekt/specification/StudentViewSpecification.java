package ee.taltech.veebirakenduseprojekt.specification;

import ee.taltech.veebirakenduseprojekt.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;


public class StudentViewSpecification {

    public static final String ROLES = "roles";
    public static final String ROLE_ID = "roleId";
    public static final String STUDENT = "student";
    public static final String COURSE = "course";

    private StudentViewSpecification() { }

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
            Join<User, UserRole> role = root.join(ROLES);
            Join<User, Declaration> dec = root.join("declarations");

            assert query != null;
            Subquery<Long> inactiveSub = query.subquery(Long.class);
            Root<Inactive> inactiveRoot = inactiveSub.from(Inactive.class);
            inactiveSub.select(inactiveRoot.get(STUDENT).get("id"))
                    .where(
                            cb.equal(inactiveRoot.get(COURSE).get("id"), courseId),
                            cb.equal(inactiveRoot.get(STUDENT).get("id"), root.get("id"))
                    );

            return cb.and(
                    cb.equal(role.get(COURSE).get("id"), courseId),
                    cb.equal(role.get(ROLE_ID), 5),
                    cb.equal(dec.get(COURSE).get("id"), courseId),
                    cb.not(cb.exists(inactiveSub))
            );
        };
    }


    public static Specification<User> studentsWithoutDeclaration(Long courseId) {
        return (root, query, cb) -> {

            Join<User, UserRole> role = root.join(ROLES);

            assert query != null;
            Subquery<Long> declarationSub = query.subquery(Long.class);
            Root<Declaration> decRoot = declarationSub.from(Declaration.class);
            declarationSub.select(decRoot.get(STUDENT).get("id"))
                    .where(
                            cb.equal(decRoot.get(COURSE).get("id"), courseId),
                            cb.equal(decRoot.get(STUDENT).get("id"), root.get("id"))
                    );

            return cb.and(
                    cb.equal(role.get(COURSE).get("id"), courseId),
                    cb.equal(role.get(ROLE_ID), 5),
                    cb.not(cb.exists(declarationSub))
            );
        };
    }

    public static Specification<User> inactiveStudentsWithDeclaration(Long courseId) {
        return (root, query, cb) -> {

            Join<User, UserRole> role = root.join(ROLES);
            Join<User, Declaration> dec = root.join("declarations");
            Join<User, Inactive> inactive = root.join("inactiveRecords");

            return cb.and(
                    cb.equal(role.get(COURSE).get("id"), courseId),
                    cb.equal(role.get(ROLE_ID), 5),
                    cb.equal(dec.get(COURSE).get("id"), courseId),
                    cb.equal(inactive.get(COURSE).get("id"), courseId)
            );
        };
    }
}
