package com.valoexplore.backend.payload;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int page,
        int limit,
        long totalItems,
        int totalPages,
        boolean last
) {}