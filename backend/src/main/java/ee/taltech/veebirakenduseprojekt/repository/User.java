package ee.taltech.veebirakenduseprojekt.repository;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;
import lombok.Builder.Default;
import org.mapstruct.*;

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

    @Schema(description = "The unique ID of the user (student or teacher)",
            example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "The user's first name", example = "Diana")
    private String firstName;

    @Schema(description = "The user's last name", example = "Lee")
    private String lastName;

    @Schema(description = "The user's email address", example = "diale@taltech.ee")
    private String email;

    @Schema(description = "The list of user's roles", example = "List<UserRole, UserRole>")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Default
    private List<UserRole> roles = new ArrayList<>();

    @Schema(description = "The list of user's comments. " +
            "Only users in student role have these populated.",
            example = "List<Comment, Comment>")
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @Default
    private List<Comment> comments = new ArrayList<>();

    @Schema(description = "The list of user's grades. ",
            example = "List<GradeItem, GradeItem>")
    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Default
    private List<GradeItem> grades = new ArrayList<>();
}
