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

    public CommentDto getComment(Long id) {
        log.debug("Getting comment with id " + id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Comment not found for student with id " + id));
                return commentMapper.mapToDto(comment);
    }

    public List<CommentDto> getCommentsByStudentId(Long studentId) {
        log.debug("Getting comments of student with id " + studentId);
        if (!studentRepository.existsById(studentId)) {
            throw new ApplicationException("Student not found with id " + studentId);
        }
        return commentRepository.findByStudentId(studentId).stream()
                .map(commentMapper::mapToDto)
                .toList();
    }

    public CommentDto addComment(CommentDto dto) {
        Comment entity = commentMapper.mapToEntity(dto, studentRepository, studentMapper);
        if (entity.getStudent() == null) {
            throw new ApplicationException("Student information is required for a comment.");
        }
        Comment saved = commentRepository.save(entity);
        return commentMapper.mapToDto(saved);
    }

    public CommentDto updateComment(Long id, CommentDto dto) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException("Comment not found with id " + id));
        existing.setCommentText(dto.getCommentText());
        existing.setType(dto.getType());
        existing.setDateTime(dto.getDateTime());
        existing.setTeacherId(dto.getTeacherId());
        Comment updated = commentRepository.save(existing);
        return commentMapper.mapToDto(updated);
    }

}
