package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.mapper.GradeItemMapper;
import ee.taltech.veebirakenduseprojekt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.*;
import java.util.*;
import java.util.stream.*;

@Service
@RequiredArgsConstructor
public class GradeItemService {

    private final GradeItemRepository gradeItemRepository;
    private final GradeItemMapper gradeItemMapper;

    public List<GradeItemDto> getAllStudentGradesByStudentId(Long courseId, Long studentId) {
        return gradeItemRepository.findAllByCourseIdAndUserId(courseId, studentId).stream()
                .map(gradeItemMapper::mapToDto)
                .toList();
    }

    public List<StudentProgressLineDto> getCourseProgressLineData(Long courseId) {
        List<GradeItem> gradeItems = gradeItemRepository.findProgressLineDataByCourseId(courseId);

        Map<Long, List<GradeItem>> gradesByUser = gradeItems.stream()
                .collect(Collectors.groupingBy(GradeItem::getUserId, LinkedHashMap::new, Collectors.toList()));

        List<StudentProgressLineDto> chartData = new ArrayList<>();

        gradesByUser.forEach((userId, userGrades) -> {
            StudentProgressLineDto studentChart = new StudentProgressLineDto();

            studentChart.setStudentId(userId);

            studentChart.setFirstName(userGrades.get(0).getStudent().getFirstName());
            studentChart.setLastName(userGrades.get(0).getStudent().getLastName());

            List<GradeDataPointDto> dataPoints = new ArrayList<>();
            BigDecimal cumulativeScore = BigDecimal.ZERO;

            for (GradeItem grade : userGrades) {
                if (grade.getGrade() != null) {
                    cumulativeScore = cumulativeScore.add(grade.getGrade());
                }
                dataPoints.add(new GradeDataPointDto(grade.getCreatedAt(), cumulativeScore));
            }

            studentChart.setDataPoints(dataPoints);
            chartData.add(studentChart);
        });
        return chartData;
    }

}
