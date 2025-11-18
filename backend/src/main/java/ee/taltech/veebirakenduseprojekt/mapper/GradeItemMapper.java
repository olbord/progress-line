package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.entity.GradeItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeItemMapper {
    @Mapping(source = "createdAt", target = "gradeDateGraded")
    @Mapping(source = "course.id", target = "courseId")
    GradeItemDto mapToDto(GradeItem gradeItem);

    @Mapping(source = "createdAt", target = "gradeDateGraded")
    LastGradeDto gradeItemEntityToLastGradeDto(GradeItem gradeItem);
}
