package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.mapper.StudentMapper;
import ee.taltech.veebirakenduseprojekt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<StudentDto> getAllStudentsWithoutDeclarationByCourse(Long courseId) {
        //Business logic: find all students without declaration and show them in a different view
        List<StudentDto> studentDtos = studentRepository
                .findAllWithoutDeclarationByCourse(courseId)
                .stream()
                .map(studentMapper::mapToDto)
                .toList();
        return studentDtos;
    }

    @Cacheable(value = "studentPersonalData", key="#id")
    public StudentPersonalDataResponseDto getStudentPersonalDataById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::mapToPersonalDataResponseDto)
                .orElseThrow(() -> new RuntimeException("Student not found for personal data: " + id));
    }

    public List<StudentDto> getAllActiveStudentsWithDeclarationByCourse(Long courseId) {
        List<StudentDto> studentDtos = studentRepository
                .findAllActiveWithDeclarationByCourse(courseId)
                .stream()
                .map(studentMapper::mapToDto)
                .toList();
        return studentDtos;
    }

    public List<StudentDto> getAllInactiveStudentsWithDeclarationByCourse(Long courseId) {
        List<StudentDto> studentDtos = studentRepository
                .findAllInactiveWithDeclarationByCourse(courseId)
                .stream()
                .map(studentMapper::mapToDto)
                .toList();
        return studentDtos;
    }
}
