package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.dto.CourseDto;
import ee.taltech.veebirakenduseprojekt.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {
    CourseDto toDto(Course course);
    List<CourseDto> toDtoList(List<Course> courses);
}
