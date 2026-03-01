package com.valoexplore.backend.payload;

import java.util.List;

public record SkinResponse(
        String id,
        String displayName,
        Integer price,
        String type,
        String source,
        Integer levelsUnlocked,
        Integer totalLevels,
        String displayIcon,
        List<String> variants,
        List<String> swatches
) {}