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
    CommentResponseDto mapToResponseDto(Comment comment);

    @Mapping(target = "student", expression = "java(studentMapper.studentIdToEntity(dto.getStudentId(), studentRepository))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    Comment mapToEntity(CommentRequestDto dto, @Context StudentRepository studentRepository, @Context StudentMapper studentMapper);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    void updateEntity(CommentRequestDto dto, @MappingTarget Comment entity, @Context StudentMapper studentMapper);

}
