package com.valoexplore.backend.payload;

import java.util.List;

public record RankedDetailsResponse(
        String tierName,
        String tierIcon,
        Integer rr,
        String lastChange,
        Double winRate,
        SeasonRankResponse highestRank,
        List<SeasonRankResponse> seasonRanks
) {}