package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.dto.TeacherDto;
import ee.taltech.veebirakenduseprojekt.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {

    TeacherDto mapToDto(User user);
}
