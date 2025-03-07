package com.suraj.comment_microservice.controller;

import com.suraj.comment_microservice.payload.CommentDTO;
import com.suraj.comment_microservice.payload.PagedResponse;
import com.suraj.comment_microservice.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.createComment(commentDTO), HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PagedResponse<CommentDTO>> fetchCommentsByPost(
            @PathVariable UUID postId,
            Pageable pageable  // ✅ Spring automatically extracts ?page=0&size=10&sort=id,asc
    ) {
        return ResponseEntity.ok(commentService.fetchCommentsByPost(postId, pageable));
    }

    //    @GetMapping("/posts/{postId}/")
//    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(
//            @PathVariable UUID postId,
//            @RequestParam( defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "asc") String sortOrder
//    ) {
//        return new ResponseEntity<>(commentService.getCommentsByPostId(postId,page,size,sortBy,sortOrder), HttpStatus.OK);
//    }


    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> fetchCommentById(@PathVariable UUID commentId) {
        return ResponseEntity.ok(commentService.fetchCommentById(commentId));
    }


    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable UUID commentId, @RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.updateComment(commentId, commentDTO));
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable UUID commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}
