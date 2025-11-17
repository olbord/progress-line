package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.entity.*;
import ee.taltech.veebirakenduseprojekt.mapper.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository responseRepository;
    private final FeedbackMapper feedbackMapper;

    public List<FeedbackResponseDto> getStudentFeedback(Long courseId, Long studentId) {

        List<FeedbackResponse> responses = responseRepository
                .findAllStudentResponsesByCourseAndStudentId(courseId, studentId);

        if (responses.isEmpty()) {
            log.info("No feedback responses found for student {} on course {}", studentId, courseId);
        }

        return responses.stream()
                .map(feedbackMapper::mapToDto)
                .toList();
    }
}
