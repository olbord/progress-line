package ee.taltech.veebirakenduseprojekt.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;
import lombok.Builder.Default;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "\"user\"")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Default
    private List<UserRole> roles = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Default
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Default
    private List<GradeItem> grades = new ArrayList<>();
}
