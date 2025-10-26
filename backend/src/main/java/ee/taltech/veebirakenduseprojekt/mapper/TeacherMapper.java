package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.controller.TeacherDto;
import ee.taltech.veebirakenduseprojekt.repository.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {

    TeacherDto mapToDto(User user);
}
