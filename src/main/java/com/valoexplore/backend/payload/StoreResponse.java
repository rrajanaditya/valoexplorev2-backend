package com.valoexplore.backend.payload;

import java.util.List;

public record StoreResponse(
        List<StoreOfferResponse> dailyOffers,
        List<NightMarketOfferResponse> nightMarket,
        Integer vp,
        Integer rp,
        Integer kc
) {}