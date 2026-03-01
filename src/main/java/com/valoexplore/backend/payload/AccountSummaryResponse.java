package com.valoexplore.backend.payload;

public record AccountSummaryResponse(
        Long id,
        String icon,
        AccountSummaryUserData userData,
        AccountSummaryRank currentRank
) {}