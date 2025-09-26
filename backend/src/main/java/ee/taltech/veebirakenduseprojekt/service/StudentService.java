package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.StudentDto;
import ee.taltech.veebirakenduseprojekt.mapper.StudentMapper;
import ee.taltech.veebirakenduseprojekt.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public List<StudentDto> getAllStudentsWithoutDeclarations() {
        //Business logic: find all
        List<StudentDto> studentDtos = studentRepository.findAllWithoutDeclarations()
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

}
