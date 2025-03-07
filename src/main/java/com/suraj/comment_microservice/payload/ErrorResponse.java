package com.suraj.comment_microservice.payload;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String error, String message, String path, LocalDateTime timestamp) {

    public ErrorResponse(int status, String error, String message, String path) {
        this(status, error, message, path, LocalDateTime.now());
    }

}
