package com.valoexplore.backend.payload;

public record PlayerCardResponse(
        String uuid,
        String displayName,
        String largeArt
) {}