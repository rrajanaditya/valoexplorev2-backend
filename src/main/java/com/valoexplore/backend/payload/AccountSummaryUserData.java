package com.valoexplore.backend.payload;

public record AccountSummaryUserData(
        String Username,
        String GameName,
        String TagLine,
        String Shard,
        String Country
) {}