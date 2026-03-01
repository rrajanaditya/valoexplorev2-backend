package com.valoexplore.backend.payload;

public record BanResponse(
        String type,
        String reason,
        String expiry
) {}