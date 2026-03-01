package com.valoexplore.backend.payload;

import java.util.List;

public record AccountResponse(
        Long id,
        UserDetailsResponse user,
        RankedDetailsResponse ranked,
        BattlepassDetailsResponse battlepass,
        List<MatchResponse> matches
) {}