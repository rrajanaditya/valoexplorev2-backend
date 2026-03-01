package com.valoexplore.backend.controller;

import com.valoexplore.backend.payload.*;
import com.valoexplore.backend.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private final AccountService accountService;

    @PostMapping("/generate")
    public ResponseEntity<AccountResponse> generate(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(accountService.generateRandomAccount(jwt.getSubject()));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<AccountSummaryResponse>> getAllAccounts(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(accountService.getAllUserAccounts(jwt.getSubject(), page, limit, search));
    }

    @GetMapping("/{username}")
    public ResponseEntity<AccountResponse> getAccountByUsername(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String username) {
        return ResponseEntity.ok(accountService.getAccountByUsername(jwt.getSubject(), username));
    }

    @GetMapping("/{username}/skins")
    public ResponseEntity<SkinInventoryResponse> getAccountSkins(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "ALL") String filterCode,
            @RequestParam(defaultValue = "VP_DESC") String sortCode) {

        return ResponseEntity.ok(
                accountService.getPaginatedSkins(jwt.getSubject(), username, page, limit, search, filterCode, sortCode)
        );
    }

    @GetMapping("/{username}/cosmetics/cards")
    public ResponseEntity<PagedResponse<CosmeticResponse>> getAccountCards(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "") String search) {

        return ResponseEntity.ok(
                accountService.getPaginatedCards(jwt.getSubject(), username, page, limit, search)
        );
    }

    @GetMapping("/{username}/cosmetics/buddies")
    public ResponseEntity<PagedResponse<CosmeticResponse>> getAccountBuddies(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "") String search) {

        return ResponseEntity.ok(
                accountService.getPaginatedBuddies(jwt.getSubject(), username, page, limit, search)
        );
    }

    @GetMapping("/{username}/cosmetics/sprays")
    public ResponseEntity<PagedResponse<CosmeticResponse>> getAccountSprays(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "") String search) {

        return ResponseEntity.ok(
                accountService.getPaginatedSprays(jwt.getSubject(), username, page, limit, search)
        );
    }

    @GetMapping("/{username}/cosmetics/agents")
    public ResponseEntity<PagedResponse<CosmeticResponse>> getAccountAgents(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String username,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int limit,
            @RequestParam(defaultValue = "") String search) {

        return ResponseEntity.ok(
                accountService.getPaginatedAgents(jwt.getSubject(), username, page, limit, search)
        );
    }

    @GetMapping("/{username}/store")
    public ResponseEntity<StoreResponse> getAccountStore(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable String username) {

        return ResponseEntity.ok(accountService.getAccountStore(jwt.getSubject(), username));
    }
}