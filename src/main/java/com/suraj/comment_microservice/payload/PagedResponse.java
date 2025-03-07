package com.suraj.comment_microservice.payload;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean firstPage,
        boolean lastPage
) {}
