package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.mapper.StudentMapper;
import ee.taltech.veebirakenduseprojekt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public Page<StudentDto> getPaginatedStudentsWithoutDeclarationByCourse(Long courseId, Pageable pageable) {
        //Business logic: find all students without declaration and show them in a different view
        return studentRepository
                .findPaginatedWithoutDeclarationByCourse(courseId, pageable)
                .map(studentMapper::mapToDto);
    }

    @Cacheable(value = "studentPersonalData", key="#id")
    public StudentPersonalDataResponseDto getStudentPersonalDataById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::mapToPersonalDataResponseDto)
                .orElseThrow(() -> new RuntimeException("Student not found for personal data: " + id));
    }

    public Page<StudentDto> getPaginatedActiveStudentsWithDeclarationByCourse(Long courseId, Pageable pageable) {
        return studentRepository
                .findPaginatedActiveWithDeclarationByCourse(courseId, pageable)
                .map(studentMapper::mapToDto);
    }

    public Page<StudentDto> getPaginatedInactiveStudentsWithDeclarationByCourse(Long courseId, Pageable pageable) {
        return studentRepository
                .findPaginatedInactiveWithDeclarationByCourse(courseId, pageable)
                .map(studentMapper::mapToDto);
    }
}
