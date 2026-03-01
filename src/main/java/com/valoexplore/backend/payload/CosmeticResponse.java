package com.valoexplore.backend.payload;

public record CosmeticResponse(
        String id,
        String displayName,
        String image,
        Integer price
) {}