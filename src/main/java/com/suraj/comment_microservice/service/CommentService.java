package com.suraj.comment_microservice.service;

import com.suraj.comment_microservice.entity.Comment;
import com.suraj.comment_microservice.exception.ResourceNotFound;
import com.suraj.comment_microservice.payload.CommentDTO;
import com.suraj.comment_microservice.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepo;
    private final ModelMapper mapper;

    public CommentDTO createComment(CommentDTO commentDTO) {
        Comment comment = mapper.map(commentDTO, Comment.class);
        return mapper.map(commentRepo.save(comment), CommentDTO.class);
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


    public List<CommentDTO> fetchCommentsByPost(UUID postId, Pageable pageable) {
        Page<Comment> page = commentRepo.findByPostId(postId, pageable);
        return page.getContent().stream()
                .map(comment -> mapper.map(comment, CommentDTO.class))
                .collect(toList());

    }


    public CommentDTO updateComment(UUID commentId, CommentDTO updatedComment) {
        Comment existingComment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFound("Comment not found"));

        existingComment.setComment(updatedComment.comment());
        return mapper.map(commentRepo.save(existingComment), CommentDTO.class);
    }

    public String deleteComment(UUID commentId) {
        if (!commentRepo.existsById(commentId)) {
            throw new ResourceNotFound("Comment not found");
        }
        commentRepo.deleteById(commentId);
        return "Comment Deleted by id: " + commentId;
    }

    public CommentDTO fetchCommentById(UUID commentId) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFound("Comment not found"));
        return mapper.map(comment, CommentDTO.class);
    }


}
