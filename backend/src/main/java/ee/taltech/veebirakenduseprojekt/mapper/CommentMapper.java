package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    @Mapping(source = "comment", target = "commentText")
    CommentDto mapToDto(Comment comment);

    @Mapping(source = "commentText", target = "comment")
    Comment mapToEntity(CommentDto dto);
}
