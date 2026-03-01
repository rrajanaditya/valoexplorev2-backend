package com.valoexplore.backend.payload;

public record MatchResponse(
        Long id,
        String agent,
        String agentName,
        String map,
        String result,
        String score,
        String kda,
        Integer combatScore
) {}