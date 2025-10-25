package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.StudentDto;
import ee.taltech.veebirakenduseprojekt.exception.ApplicationException;
import ee.taltech.veebirakenduseprojekt.mapper.StudentMapper;
import ee.taltech.veebirakenduseprojekt.repository.Student;
import ee.taltech.veebirakenduseprojekt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public StudentDto getStudent(Long id) {
        return studentRepository.findById(id)
                .map(studentMapper::mapToDto)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
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

    public StudentDto getStudentWithGrades(String firstName, String lastName) {
        Student student = studentRepository.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new ApplicationException("Student not found: " + firstName + " " + lastName));
        return studentMapper.mapToDto(student);
    }
}
