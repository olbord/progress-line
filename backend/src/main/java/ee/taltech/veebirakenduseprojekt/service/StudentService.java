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

    public List<StudentDto> getAllStudentsWithStatuses() {
        List<StudentDto> studentDtos = studentRepository.findAllWithComments()
                .stream()
                .map(studentMapper::toDto)
                .toList();

        // TODO: calculate statuses based on students' percentiles
        //  and dates of last added comment

        return studentDtos;
    }
}
