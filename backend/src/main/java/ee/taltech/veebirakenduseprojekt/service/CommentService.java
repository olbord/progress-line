package ee.taltech.veebirakenduseprojekt.service;

import java.util.*;

import ee.taltech.veebirakenduseprojekt.exception.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.stereotype.*;
import ee.taltech.veebirakenduseprojekt.controller.*;
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

    public List<CommentResponseDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> commentMapper.mapToResponseDto(comment))
                .toList();
    }

    public CommentResponseDto getComment(Long id) {
        log.debug("Getting comment with id " + id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Comment not found for student with id " + id));
                return commentMapper.mapToResponseDto(comment);
    }

    public List<CommentResponseDto> getCommentsByStudentId(Long studentId) {
        log.debug("Getting comments of student with id " + studentId);
        if (!studentRepository.existsById(studentId)) {
            throw new ApplicationException("Student not found with id " + studentId);
        }
        return commentRepository.findByStudentId(studentId).stream()
                .map(commentMapper::mapToResponseDto)
                .toList();
    }

    public CommentResponseDto addComment(CommentRequestDto dto) {
        Long studentId = dto.getStudentId();
        if (studentId == null) {
            throw new ApplicationException("Student ID cannot be null for a new comment");
        }
        Student student = studentMapper.studentIdToEntity(studentId, studentRepository);
        Comment entity = commentMapper.mapToEntity(dto, studentRepository, studentMapper);
        if (entity.getStudent() == null) {
            throw new ApplicationException("Student information is required for a comment.");
        }
        entity.setStudent(student);
        Comment savedComment = commentRepository.save(entity);
        return commentMapper.mapToResponseDto(savedComment);
    }

    public CommentResponseDto updateComment(Long id, CommentRequestDto dto) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Comment not found with id " + id));
        commentMapper.updateEntity(dto, existing, studentMapper);
        Comment updated = commentRepository.save(existing);
        return commentMapper.mapToResponseDto(updated);
    }

}
