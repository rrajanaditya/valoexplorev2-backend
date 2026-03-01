package com.valoexplore.backend.payload;

public record SkinInventoryResponse(
        PagedResponse<SkinResponse> inventory,
        Long totalVpSpent
) {}