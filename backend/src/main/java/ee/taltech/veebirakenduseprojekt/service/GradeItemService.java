package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.GradeItemDto;
import ee.taltech.veebirakenduseprojekt.mapper.GradeItemMapper;
import ee.taltech.veebirakenduseprojekt.repository.GradeItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradeItemService {

    private final GradeItemRepository gradeItemRepository;
    private final GradeItemMapper gradeItemMapper;

    public List<GradeItemDto> getAllStudentGradesByStudentId(Long studentId) {
        return gradeItemRepository.findAllByStudentId(studentId).stream()
                .map(gradeItemMapper::mapToDto)
                .toList();
    }
}
