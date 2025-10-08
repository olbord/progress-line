package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {StudentMapper.class})
public interface CommentMapper {

    @Mapping(source = "student", target = "student")
    @Mapping(source = "teacherId", target = "teacherId")
    CommentDto mapToDto(Comment comment);

    @Mapping(target = "student", expression = "java(studentMapper.studentDtoToEntityFromCommentDto(dto.getStudent(), studentRepository))")
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "teacherId", target = "teacherId")
    Comment mapToEntity(CommentDto dto, @Context StudentRepository studentRepository, @Context StudentMapper studentMapper);
}
