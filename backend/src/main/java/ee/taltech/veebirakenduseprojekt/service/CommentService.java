package ee.taltech.veebirakenduseprojekt.service;

import java.util.*;

import ee.taltech.veebirakenduseprojekt.dto.*;
import ee.taltech.veebirakenduseprojekt.entity.Comment;
import ee.taltech.veebirakenduseprojekt.entity.User;
import ee.taltech.veebirakenduseprojekt.exception.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import ee.taltech.veebirakenduseprojekt.mapper.*;
import ee.taltech.veebirakenduseprojekt.repository.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final CourseRepository courseRepository;

    public List<CommentResponseDto> getAllComments() {
        List<Comment> comments = commentRepository.findAllWithStudents();
        return comments.stream()
                .map(commentMapper::mapToResponseDto)
                .toList();
    }

    public CommentResponseDto addComment(CommentRequestDto dto) {
        Long studentId = dto.getStudentId();
        User student = studentMapper.studentIdToEntity(studentId, studentRepository);
        Comment entity = commentMapper.mapToEntity(dto, studentRepository, studentMapper, courseRepository);
        entity.setStudent(student);
        Comment savedComment = commentRepository.save(entity);
        return commentMapper.mapToResponseDto(savedComment);
    }

    public List<CommentResponseDto> getCommentsByCourseIdAndStudentId(Long courseId, Long studentId) {
        log.debug("Getting comments of student with id " + studentId);
        if (!studentRepository.existsById(studentId)) {
            throw new ApplicationException("Student not found with id " + studentId);
        }
        return commentRepository.findByStudentIdAndCourseId(courseId, studentId)
                .stream()
                .map(commentMapper::mapToResponseDto)
                .toList();
    }
}
