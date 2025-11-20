package ee.taltech.veebirakenduseprojekt.entity;

import io.swagger.v3.oas.annotations.media.*;

@Schema(name = "Comment Type Enum",
        description = "Types of interventions to support students progress.")
public enum CommentType {
    EMAIL,
    DISCORD,
    STUDY_GROUP,
    CONSULTATION,
    OTHER
}
