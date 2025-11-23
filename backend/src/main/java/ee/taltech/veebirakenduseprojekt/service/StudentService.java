package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.entity.StudentView;
import ee.taltech.veebirakenduseprojekt.entity.User;
import ee.taltech.veebirakenduseprojekt.mapper.StudentMapper;
import ee.taltech.veebirakenduseprojekt.repository.StudentRepository;
import ee.taltech.veebirakenduseprojekt.repository.StudentViewRepository;
import ee.taltech.veebirakenduseprojekt.specification.StudentViewSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final StudentViewRepository studentViewRepository;


    public Page<StudentDto> getPaginatedStudentsWithoutDeclarationByCourse(
            Long courseId,
            String searchTerm,
            Pageable pageable
    ) {
        //Business logic: find all students without declaration and show them in a different view
        Specification<User> spec = Specification.allOf(
                StudentViewSpecification.studentsWithoutDeclaration(courseId),
                StudentViewSpecification.search(searchTerm)
        );

        List<Long> userIds = studentRepository.findAll(spec)
                .stream()
                .map(User::getId)
                .toList();

        if (userIds.isEmpty()) {
            return Page.empty(pageable);
        }

        Page<StudentView> viewPage = studentViewRepository
                .findByIdUserIdInAndIdCourseId(userIds, courseId, pageable);

        return viewPage.map(studentMapper::mapFromView);
    }

    @Cacheable(value = "studentPersonalData", key="#id")
    public StudentPersonalDataResponseDto getStudentPersonalDataById(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::mapToPersonalDataResponseDto)
                .orElseThrow(() -> new RuntimeException("Student not found for personal data: " + id));
    }

    public Page<StudentDto> getPaginatedActiveStudentsWithDeclarationByCourse(
            Long courseId,
            String searchTerm,
            Pageable pageable
    ) {
        Specification<User> spec = Specification.allOf(
                StudentViewSpecification.activeStudents(courseId),
                StudentViewSpecification.search(searchTerm)
        );

        List<Long> userIds = studentRepository.findAll(spec)
                .stream()
                .map(User::getId)
                .toList();


        if (userIds.isEmpty()) {
            return Page.empty(pageable);
        }

        Page<StudentView> viewPage = studentViewRepository
                .findByIdUserIdInAndIdCourseId(userIds, courseId, pageable);

        return viewPage.map(studentMapper::mapFromView);
    }

    public Page<StudentDto> getPaginatedInactiveStudentsWithDeclarationByCourse(
            Long courseId,
            String searchTerm,
            Pageable pageable
    ) {
        Specification<User> spec = Specification.allOf(
                StudentViewSpecification.inactiveStudentsWithDeclaration(courseId),
                StudentViewSpecification.search(searchTerm)
        );

        List<Long> userIds = studentRepository.findAll(spec)
                .stream()
                .map(User::getId)
                .toList();

        if (userIds.isEmpty()) {
            return Page.empty(pageable);
        }

        Page<StudentView> viewPage = studentViewRepository
                .findByIdUserIdInAndIdCourseId(userIds, courseId, pageable);

        return viewPage.map(studentMapper::mapFromView);
    }
}
