package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.controller.CourseDto;
import ee.taltech.veebirakenduseprojekt.repository.Course;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseDto toDto(Course course);
    List<CourseDto> toDtoList(List<Course> courses);
}
