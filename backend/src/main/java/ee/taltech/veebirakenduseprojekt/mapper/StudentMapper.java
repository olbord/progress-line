package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.controller.StudentDto;
import ee.taltech.veebirakenduseprojekt.repository.Comment;
import ee.taltech.veebirakenduseprojekt.repository.Student;
import org.mapstruct.*;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(source = "id", target = "userId")
    //@Mapping(source = "firstName", target = "firstName")
    //@Mapping(source = "lastName", target = "lastName")
    //@Mapping(source = "email", target = "email")
    //@Mapping(source = "comments", target = "commentDateTimes")
    StudentDto mapToDto(Student student);

    default List<LocalDateTime> mapComments(List<Comment> comments) {
        return comments.stream()
                .map(Comment::getDateTime)
                .toList();
    }
}

