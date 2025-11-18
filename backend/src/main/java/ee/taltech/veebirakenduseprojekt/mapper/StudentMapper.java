package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.exception.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import org.mapstruct.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {GradeItemMapper.class})
public interface StudentMapper {


    StudentDto mapToDto(User student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    User mapToEntity(StudentDto dto);

    default List<LocalDateTime> mapComments(List<Comment> comments) {
        return comments.stream()
                .map(Comment::getDateTime)
                .toList();
    }

    @Named("studentDtoToEntityFromCommentDto")
    default User studentDtoToEntityFromCommentDto(StudentDto studentDto, @Context StudentRepository studentRepository) {
        if (studentDto == null) {
            return null;
        }
        return studentRepository.findById(studentDto.getId())
                .orElseThrow(() -> new ApplicationException("Student not found with ID: " + studentDto.getId()));
    }

    @Named("studentIdToEntity")
    default User studentIdToEntity(Long studentId, @Context StudentRepository studentRepository) {
        if (studentId == null) {
            return null;
        }
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new ApplicationException("Student not found with ID: " + studentId));
    }

    StudentPersonalDataResponseDto mapToPersonalDataResponseDto(User entity);
}

