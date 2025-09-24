package ee.taltech.veebirakenduseprojekt.service;

import ee.taltech.veebirakenduseprojekt.controller.*;
import ee.taltech.veebirakenduseprojekt.mapper.*;
import ee.taltech.veebirakenduseprojekt.repository.*;
import lombok.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentDto getComment(Long id) {
        Comment comment = commentRepository.findById(id).get();
        return commentMapper.mapToDto(comment);
    }



    //public List<Comment> getCommentsByUser(Long userId) {
        //return commentRepository.findByUserId(userId);
    //}

}
