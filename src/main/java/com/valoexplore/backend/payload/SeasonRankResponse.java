package com.valoexplore.backend.payload;

public record SeasonRankResponse(
        String season,
        String tierName,
        String tierIcon
) {}