package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.controller.GradeItemDto;
import ee.taltech.veebirakenduseprojekt.repository.GradeItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GradeItemMapper {
    @Mapping(source = "createdAt", target = "gradeDateGraded")
    GradeItemDto mapToDto(GradeItem gradeItem);
}
