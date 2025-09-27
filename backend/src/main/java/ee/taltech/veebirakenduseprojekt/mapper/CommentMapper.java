package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "comment", target = "commentText")
    @Mapping(source = "student.id", target = "userId")
    CommentDto mapToDto(Comment comment);

    @Mapping(source = "commentText", target = "comment")
    @Mapping(source = "userId", target = "student", qualifiedByName = "mapStudent")
    Comment mapToEntity(CommentDto dto);

    @Named("mapStudent")
    default Student mapStudent(Long userId) {
        if (userId == null) return null;
        Student s = new Student();
        s.setId(userId);
        return s;
    }
}
