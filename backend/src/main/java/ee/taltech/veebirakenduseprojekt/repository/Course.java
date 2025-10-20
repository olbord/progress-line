package ee.taltech.veebirakenduseprojekt.repository;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Getter
public class Course {
    @Id
    private Long id;

    @OneToMany(mappedBy = "course")
    private Set<Inactive> inactives;

    @OneToMany(mappedBy = "course")
    private Set<Declaration> declarations;

    @OneToMany(mappedBy = "course")
    private Set<MoodleEnrollment> enrolledUsers;
}
