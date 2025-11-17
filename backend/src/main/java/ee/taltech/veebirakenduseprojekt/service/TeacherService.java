package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.TeacherDto;
import ee.taltech.veebirakenduseprojekt.exception.*;
import ee.taltech.veebirakenduseprojekt.mapper.TeacherMapper;
import ee.taltech.veebirakenduseprojekt.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public List<TeacherDto> getTeachersByCourse(Long courseId) {
        return teacherRepository.getTeachersByCourse(courseId)
                .stream()
                .map(teacherMapper::mapToDto)
                .toList();
    }

    public User getTeacherByEmail(String email) {
        return teacherRepository.findByEmail(email);
    }
}
