package com.valoexplore.backend.payload;

public record BattlepassDetailsResponse(
        Integer currentTier,
        Integer maxTier,
        Integer xpProgress
) {}