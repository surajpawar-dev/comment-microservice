package com.suraj.comment_microservice.service;

import com.suraj.comment_microservice.entity.Comment;
import com.suraj.comment_microservice.exception.ResourceNotFoundException;
import com.suraj.comment_microservice.payload.CommentDTO;
import com.suraj.comment_microservice.payload.PagedResponse;
import com.suraj.comment_microservice.repository.CommentRepository;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final ModelMapper mapper;

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = mapper.map(commentDTO, Comment.class);
        Comment saved = commentRepo.save(comment);
        return mapper.map(saved, CommentDTO.class);
    }

//    public List<CommentDTO> getCommentsByPostId(UUID postId, int pageNumber, int pageSize, String sortBy, String sortOrder) {
//
//        if (pageNumber < 0 || pageSize < 0) {
//            throw new IllegalArgumentException("Page number and page size must be non-negative");
//        }
//
//        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Sort sort = Sort.by(direction, sortBy);
//
//        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
//
//        Page<Comment> comments = commentRepo.findByPostId(postId, pageable);
//
//        // ✅ Return an empty list instead of throwing an exception
//        return comments.getContent().stream()
//                .map(comment -> mapper.map(comment, CommentDTO.class))
//                .collect(Collectors.toList());
//    }


    public PagedResponse<CommentDTO> fetchCommentsByPost(UUID postId, Pageable pageable) {
        Page<Comment> pageResult = commentRepo.findByPostId(postId, pageable);
        List<CommentDTO> comments = pageResult.stream().
                map(comment -> mapper.map(comment, CommentDTO.class))
                .collect(toList());

        PagedResponse<CommentDTO> response = new PagedResponse<CommentDTO>(
                comments,
                pageResult.getPageable().getPageNumber(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isFirst(),
                pageResult.isLast()
        );
        return response;
    }


    public CommentDTO updateComment(UUID commentId, CommentDTO updatedComment) {
        Comment existingComment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        existingComment.setComment(updatedComment.getComment());
        return mapper.map(commentRepo.save(existingComment), CommentDTO.class);
    }

    public String deleteComment(UUID commentId) {
        if (!commentRepo.existsById(commentId)) {
            throw new ResourceNotFoundException("Comment not found");
        }
        commentRepo.deleteById(commentId);
        return "Comment Deleted by id: " + commentId;
    }

    public CommentDTO fetchCommentById(UUID commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        return mapper.map(comment, CommentDTO.class);
    }


}
