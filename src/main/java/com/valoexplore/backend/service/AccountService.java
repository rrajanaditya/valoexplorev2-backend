package com.valoexplore.backend.service;

import com.valoexplore.backend.payload.*;

public interface AccountService {
    AccountResponse generateRandomAccount(String auth0Id);
    PagedResponse<AccountSummaryResponse> getAllUserAccounts(String auth0Id, int page, int limit, String searchQuery);
    AccountResponse getAccountByUsername(String auth0Id, String username);
    StoreResponse getAccountStore(String auth0Id, String username);
    SkinInventoryResponse getPaginatedSkins(String auth0Id, String username, int page, int limit, String search, String filterCode, String sortCode);
    PagedResponse<CosmeticResponse> getPaginatedCards(String auth0Id, String username, int page, int limit, String search);
    PagedResponse<CosmeticResponse> getPaginatedBuddies(String auth0Id, String username, int page, int limit, String search);
    PagedResponse<CosmeticResponse> getPaginatedSprays(String auth0Id, String username, int page, int limit, String search);
    PagedResponse<CosmeticResponse> getPaginatedAgents(String auth0Id, String username, int page, int limit, String search);
}