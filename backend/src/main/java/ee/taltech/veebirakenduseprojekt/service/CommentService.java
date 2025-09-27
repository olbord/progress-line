package ee.taltech.veebirakenduseprojekt.service;

import java.util.*;
import lombok.*;
import org.springframework.stereotype.*;
import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.mapper.*;
import ee.taltech.veebirakenduseprojekt.repository.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentDto getComment(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::mapToDto)
                .orElseThrow(() -> new RuntimeException("Comment not found with id " + id));
    }

    public List<CommentDto> getCommentsByStudentId(Long studentId) {
        return commentRepository.findByStudentId(studentId).stream()
                .map(commentMapper::mapToDto)
                .toList();
    }



    public CommentDto addComment(CommentDto dto) {
        Comment entity = commentMapper.mapToEntity(dto);
        Comment saved = commentRepository.save(entity);
        return commentMapper.mapToDto(saved);
    }

    public CommentDto updateComment(Long id, CommentDto dto) {
        Comment existing = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id " + id));
        existing.setComment(dto.getCommentText());
        existing.setType(String.valueOf(dto.getType()));
        existing.setDateTime(dto.getDateTime());
        Comment updated = commentRepository.save(existing);
        return commentMapper.mapToDto(updated);
    }

}
