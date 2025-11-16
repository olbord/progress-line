package ee.taltech.veebirakenduseprojekt.mapper;

import ee.taltech.veebirakenduseprojekt.dto.*;
import org.mapstruct.*;
import ee.taltech.veebirakenduseprojekt.entity.*;

@Mapper(componentModel = "spring",
       unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedbackMapper {

    @Mapping(source = "id", target = "responseId")
    @Mapping(source = "name", target = "questionName")
    @Mapping(source = "printval", target = "printValue")
    @Mapping(source = "attempt.feedback.name", target = "feedbackName")
    @Mapping(source = "createdAt", target = "responseTime")
    FeedbackResponseDto mapToDto(FeedbackResponse response);
}
