package com.suraj.comment_microservice.payload;

import java.util.UUID;

public record CommentDTO(UUID id, UUID postId, String comment) {
}
