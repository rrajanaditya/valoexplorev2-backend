package com.valoexplore.backend.payload;

import java.util.List;

public record StoreOfferResponse(
        String id,
        String displayName,
        String type,
        Integer levelsUnlocked,
        Integer totalLevels,
        String image,
        List<String> variants,
        List<String> swatches,
        Integer price
) {}