package com.valoexplore.backend.payload;

import java.util.List;

public record UserDetailsResponse(
        String username,
        String tag,
        Integer level,
        String title,
        String country,
        Boolean emailVerified,
        Boolean phoneVerified,
        String creationDate,
        String banner,
        String avatar,
        String region,
        List<AliasResponse> previousAliases,
        List<BanResponse> bans
) {}