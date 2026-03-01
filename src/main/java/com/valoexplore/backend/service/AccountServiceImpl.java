package com.valoexplore.backend.service;

import com.valoexplore.backend.model.*;
import com.valoexplore.backend.payload.*;
import com.valoexplore.backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ValorantAccountRepository accountRepo;
    private final UserRepository userRepo;
    private final SkinRepository skinRepo;
    private final CompetitiveRankRepository rankRepo;
    private final PlayerCardRepository cardRepo;
    private final BuddyRepository buddyRepo;
    private final SprayRepository sprayRepo;
    private final AgentRepository agentRepo;

    private final Logger logger =  LoggerFactory.getLogger(AccountServiceImpl.class);

    private static final String[] USERNAMES = {
            "Shadow", "Lurker", "VandalGod", "ToxicJett", "HealMeSage", "Carry",
            "Smurf", "TenZ", "Shroud", "NoobSlayer", "AimBot", "Flick", "ClutchKing",
            "ViperMain", "PeekMe", "InstalockReyna", "Whiff", "RadiantDreams"
    };
    private static final String[] TITLES = {
            "Vanguard", "Flex", "Spike Rusher", "Deadeye", "God Tier", "Potato",
            "Unstoppable", "Clutch Master", "Initiator", "Duelist", "Sentinel"
    };
    private static final String[] REGIONS = {"NA", "EU", "AP", "KR", "BR"};
    private static final String[] MAPS = {"Ascent", "Bind", "Haven", "Split", "Icebox", "Breeze", "Fracture", "Lotus", "Pearl", "Sunset"};

    @Override
    @Transactional
    public AccountResponse generateRandomAccount(String auth0Id) {
        User user = userRepo.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Random rand = new Random();
        ValorantAccount acc = new ValorantAccount();

        acc.setUser(user);
        acc.setUsername(USERNAMES[rand.nextInt(USERNAMES.length)] + (rand.nextInt(900) + 100));
        acc.setTagline(String.format("%04d", rand.nextInt(10000)));
        acc.setLevel(rand.nextInt(400) + 10);
        acc.setTitle(TITLES[rand.nextInt(TITLES.length)]);

        int regionIdx = rand.nextInt(REGIONS.length);
        acc.setRegion(REGIONS[regionIdx]);
        acc.setCountry(regionIdx == 0 ? "US" : regionIdx == 1 ? "GB" : regionIdx == 2 ? "JP" : regionIdx == 3 ? "KR" : "BR");

        acc.setEmailVerified(rand.nextInt(10) > 1);
        acc.setPhoneVerified(rand.nextBoolean());
        acc.setAccountCreationDate(LocalDateTime.now().minusDays(rand.nextInt(1000)));

        BattlepassStats bp = new BattlepassStats();
        bp.setBpCurrentTier(rand.nextInt(50) + 1);
        bp.setBpMaxTier(50);
        bp.setBpXpProgress(rand.nextInt(30000));
        acc.setBattlepass(bp);

        acc.setRr(rand.nextInt(100));
        acc.setWinRate(40.0 + (rand.nextDouble() * 20.0));
        int rrChange = rand.nextInt(30) + 10;
        boolean wasWin = rand.nextBoolean();
        acc.setLastRankChangeString((wasWin ? "+" : "-") + rrChange + " RR");

        List<CompetitiveRank> randomRanks = rankRepo.findRandomRanks(5);
        if (!randomRanks.isEmpty()) {
            acc.setCurrentRank(randomRanks.getFirst());
            acc.setHighestRank(randomRanks.getFirst());
        }

        for (int i = 1; i < Math.min(4, randomRanks.size()); i++) {
            AccountSeasonRank seasonRank = new AccountSeasonRank();
            seasonRank.setSeason("E" + (rand.nextInt(7) + 1) + "A" + (rand.nextInt(3) + 1));
            seasonRank.setRank(randomRanks.get(i));
            seasonRank.setAccount(acc);
            acc.getSeasonRanks().add(seasonRank);
        }
        List<Agent> allAgents = agentRepo.findAll();
        Collections.shuffle(allAgents);
        List<Agent> ownedAgents = allAgents.stream().limit(rand.nextInt(15) + 5).toList();
        acc.setOwnedAgents(ownedAgents);

        List<Buddy> allBuddies = buddyRepo.findAll();
        Collections.shuffle(allBuddies);
        acc.setOwnedBuddies(allBuddies.stream().limit(rand.nextInt(30) + 5).toList());

        List<Spray> allSprays = sprayRepo.findAll();
        Collections.shuffle(allSprays);
        acc.setOwnedSprays(allSprays.stream().limit(rand.nextInt(40) + 10).toList());

        List<PlayerCard> randomCards = cardRepo.findRandomCards(15);
        acc.getOwnedCards().addAll(randomCards);
        if (!randomCards.isEmpty()) {
            acc.setAvatarUrl(randomCards.getFirst().getLargeArt());
            acc.setBannerUrl(randomCards.getFirst().getWideArt());
        }

        acc.setOwnedSkins(skinRepo.findRandomSkins(25));
        for(int i = 0; i < 8; i++) {
            MatchHistory match = new MatchHistory();
            Agent matchAgent = ownedAgents.isEmpty() ? null : ownedAgents.get(rand.nextInt(ownedAgents.size()));
            match.setAgent(matchAgent != null ? matchAgent.getDisplayIcon() : "https://media.valorant-api.com/agents/add6443a-41bd-e414-f6ad-e58d267f4e95/displayicon.png");
            match.setAgentName(matchAgent != null ? matchAgent.getDisplayName() : "Jett");

            match.setMap(MAPS[rand.nextInt(MAPS.length)]);

            boolean isVictory = rand.nextBoolean();
            match.setResult(isVictory ? "VICTORY" : "DEFEAT");

            int roundsWon = isVictory ? 13 : rand.nextInt(12);
            int roundsLost = isVictory ? rand.nextInt(12) : 13;
            match.setScore(roundsWon + "-" + roundsLost);
            int kills = isVictory ? rand.nextInt(20) + 10 : rand.nextInt(15) + 5;
            int deaths = isVictory ? rand.nextInt(15) + 5 : rand.nextInt(20) + 10;
            int assists = rand.nextInt(15);
            match.setKda(kills + "/" + deaths + "/" + assists);
            match.setCombatScore((kills * 12) + rand.nextInt(80));

            match.setAccount(acc);
            acc.getMatches().add(match);
        }

        if (rand.nextBoolean()) {
            AccountAlias alias = new AccountAlias();
            alias.setGameName(USERNAMES[rand.nextInt(USERNAMES.length)]);
            alias.setTagLine(String.format("%04d", rand.nextInt(10000)));
            alias.setAliasCreatedAt(LocalDateTime.now().minusMonths(rand.nextInt(18) + 1));
            alias.setAccount(acc);
            acc.getPreviousAliases().add(alias);
        }

        if (rand.nextInt(100) < 15) {
            AccountBan ban = new AccountBan();
            String[] banTypes = {"CHAT_RESTRICTION", "COMPETITIVE_COOLDOWN", "AFK_WARNING"};
            String[] banReasons = {"TOXIC_BEHAVIOR", "QUEUE_DODGING", "AFK"};
            int banIdx = rand.nextInt(banTypes.length);

            ban.setType(banTypes[banIdx]);
            ban.setReason(banReasons[banIdx]);
            ban.setExpiry(LocalDateTime.now().plusDays(rand.nextInt(30)));
            ban.setAccount(acc);
            acc.getBans().add(ban);
        }

        ValorantAccount saved = accountRepo.save(acc);
        return mapToResponse(saved);
    }

    @Override
    public PagedResponse<AccountSummaryResponse> getAllUserAccounts(String auth0Id, int page, int limit, String searchQuery) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit);
        Page<ValorantAccount> accountPage = accountRepo.findByUserAuth0IdAndSearch(auth0Id, searchQuery, pageable);

        List<AccountSummaryResponse> DTOs = accountPage.getContent().stream()
                .map(this::mapToSummaryResponse)
                .toList();

        return new PagedResponse<>(
                DTOs, page, limit, accountPage.getTotalElements(), accountPage.getTotalPages(), accountPage.isLast()
        );
    }

    private AccountSummaryResponse mapToSummaryResponse(ValorantAccount acc) {
        AccountSummaryUserData userData = new AccountSummaryUserData(
                acc.getUsername(), acc.getUsername(), acc.getTagline(),
                acc.getRegion(), acc.getCountry()
        );

        AccountSummaryRank rankData = new AccountSummaryRank(
                acc.getCurrentRank() != null ? acc.getCurrentRank().getTierName() : "Unranked",
                acc.getCurrentRank() != null ? acc.getCurrentRank().getLargeIcon() : ""
        );

        return new AccountSummaryResponse(acc.getId(), acc.getAvatarUrl(), userData, rankData);
    }

    @Override
    public AccountResponse getAccountByUsername(String auth0Id, String username) {
        return accountRepo.findByUserAuth0IdAndUsername(auth0Id, username)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public SkinInventoryResponse getPaginatedSkins(String auth0Id, String username, int page, int limit, String search, String filterCode, String sortCode) {
        Sort sort = Sort.unsorted();
        if ("VP_DESC".equals(sortCode)) sort = Sort.by(Sort.Direction.DESC, "price");
        else if ("VP_ASC".equals(sortCode)) sort = Sort.by(Sort.Direction.ASC, "price");
        else if ("GUN".equals(sortCode)) sort = Sort.by(Sort.Direction.ASC, "weaponType");

        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit, sort);

        Page<Skin> skinPage = skinRepo.findAccountSkinsPaginated(auth0Id, username, search, filterCode, pageable);
        Long totalVp = skinRepo.getTotalVpSpent(auth0Id, username);
//        Long totalVp = 100L;

        List<SkinResponse> DTOs = skinPage.getContent().stream()
                .map(s -> new SkinResponse(
                        s.getUuid(), s.getDisplayName(), s.getPrice(), s.getWeaponType(),
                        s.getSource() != null ? s.getSource() : "BOUGHT",
                        1, s.getTotalLevels() != null ? s.getTotalLevels() : 1,
                        s.getDisplayIcon(), s.getVariants(), s.getSwatches()
                )).toList();

        PagedResponse<SkinResponse> pagedResponse = new PagedResponse<>(
                DTOs, page, limit, skinPage.getTotalElements(), skinPage.getTotalPages(), skinPage.isLast()
        );

        return new SkinInventoryResponse(pagedResponse, totalVp);
    }

    @Override
    public PagedResponse<CosmeticResponse> getPaginatedCards(String auth0Id, String username, int page, int limit, String search) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit);
        Page<PlayerCard> cardPage = cardRepo.findAccountCardsPaginated(auth0Id, username, search, pageable);
        List<CosmeticResponse> DTOs = cardPage.getContent().stream()
                .map(c -> new CosmeticResponse(c.getUuid(), c.getDisplayName(), c.getLargeArt(), c.getPrice()))
                .toList();
        return new PagedResponse<>(DTOs, page, limit, cardPage.getTotalElements(), cardPage.getTotalPages(), cardPage.isLast());
    }

    @Override
    public PagedResponse<CosmeticResponse> getPaginatedBuddies(String auth0Id, String username, int page, int limit, String search) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit);
        Page<Buddy> buddyPage = buddyRepo.findAccountBuddiesPaginated(auth0Id, username, search, pageable);
        List<CosmeticResponse> DTOs = buddyPage.getContent().stream()
                .map(b -> new CosmeticResponse(b.getUuid(), b.getDisplayName(), b.getDisplayIcon(), b.getPrice()))
                .toList();
        return new PagedResponse<>(DTOs, page, limit, buddyPage.getTotalElements(), buddyPage.getTotalPages(), buddyPage.isLast());
    }

    @Override
    public PagedResponse<CosmeticResponse> getPaginatedSprays(String auth0Id, String username, int page, int limit, String search) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit);
        Page<Spray> sprayPage = sprayRepo.findAccountSpraysPaginated(auth0Id, username, search, pageable);
        List<CosmeticResponse> DTOs = sprayPage.getContent().stream()
                .map(s -> new CosmeticResponse(s.getUuid(), s.getDisplayName(), s.getDisplayIcon(), s.getPrice()))
                .toList();
        return new PagedResponse<>(DTOs, page, limit, sprayPage.getTotalElements(), sprayPage.getTotalPages(), sprayPage.isLast());
    }

    @Override
    public PagedResponse<CosmeticResponse> getPaginatedAgents(String auth0Id, String username, int page, int limit, String search) {
        Pageable pageable = PageRequest.of(Math.max(0, page - 1), limit);
        Page<Agent> agentPage = agentRepo.findAccountAgentsPaginated(auth0Id, username, search, pageable);
        List<CosmeticResponse> DTOs = agentPage.getContent().stream()
                .map(a -> new CosmeticResponse(a.getUuid(), a.getDisplayName(), a.getDisplayIcon(), a.getPrice()))
                .toList();
        return new PagedResponse<>(DTOs, page, limit, agentPage.getTotalElements(), agentPage.getTotalPages(), agentPage.isLast());
    }

    private AccountResponse mapToResponse(ValorantAccount acc) {
        List<AliasResponse> aliases = acc.getPreviousAliases().stream()
                .map(a -> new AliasResponse(a.getGameName(), a.getTagLine(), a.getAliasCreatedAt().toString()))
                .toList();

        List<BanResponse> bans = acc.getBans().stream()
                .map(b -> new BanResponse(b.getType(), b.getReason(), b.getExpiry().toString()))
                .toList();

        UserDetailsResponse userDetails = new UserDetailsResponse(
                acc.getUsername(), acc.getTagline(), acc.getLevel(), acc.getTitle(),
                acc.getCountry(), acc.getEmailVerified(), acc.getPhoneVerified(),
                acc.getAccountCreationDate() != null ? acc.getAccountCreationDate().toString() : "",
                acc.getBannerUrl(), acc.getAvatarUrl(), acc.getRegion(), aliases, bans
        );

        SeasonRankResponse highestRankDTO = acc.getHighestRank() != null ?
                new SeasonRankResponse("All Time", acc.getHighestRank().getTierName(), acc.getHighestRank().getLargeIcon()) : null;

        List<SeasonRankResponse> seasonRanksDTO = acc.getSeasonRanks().stream()
                .map(sr -> new SeasonRankResponse(sr.getSeason(), sr.getRank().getTierName(), sr.getRank().getLargeIcon()))
                .toList();

        RankedDetailsResponse ranked = new RankedDetailsResponse(
                acc.getCurrentRank() != null ? acc.getCurrentRank().getTierName() : "Unranked",
                acc.getCurrentRank() != null ? acc.getCurrentRank().getLargeIcon() : "",
                acc.getRr(), acc.getLastRankChangeString() != null ? acc.getLastRankChangeString() : "+0 RR",
                acc.getWinRate(), highestRankDTO, seasonRanksDTO
        );

        BattlepassDetailsResponse bp = new BattlepassDetailsResponse(
                acc.getBattlepass() != null ? acc.getBattlepass().getBpCurrentTier() : 0,
                acc.getBattlepass() != null ? acc.getBattlepass().getBpMaxTier() : 50,
                acc.getBattlepass() != null ? acc.getBattlepass().getBpXpProgress() : 0
        );

        List<MatchResponse> matches = acc.getMatches().stream()
                .map(m -> new MatchResponse(m.getId(), m.getAgent(), m.getAgentName(), m.getMap(), m.getResult(), m.getScore(), m.getKda(), m.getCombatScore()))
                .toList();

        return new AccountResponse(acc.getId(), userDetails, ranked, bp, matches);
    }

    @Override
    public StoreResponse getAccountStore(String auth0Id, String username) {
        ValorantAccount acc = accountRepo.findByUserAuth0IdAndUsername(auth0Id, username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Skin> allSkins = skinRepo.findAll();

        String dailySeedString = acc.getId() + "-" + LocalDate.now().toString();
        Random dailyRand = new Random(dailySeedString.hashCode());

        List<Skin> dailyPool = new ArrayList<>(allSkins);
        Collections.shuffle(dailyPool, dailyRand);

        List<StoreOfferResponse> dailyOffers = dailyPool.stream().limit(4).map(s -> {
            int price = (s.getPrice() != null && s.getPrice() > 0) ? s.getPrice() : 1775;

            return new StoreOfferResponse(
                    s.getUuid(), s.getDisplayName(), s.getWeaponType(),
                    1, s.getTotalLevels() != null ? s.getTotalLevels() : 1,
                    s.getDisplayIcon(), s.getVariants(), s.getSwatches(), price
            );
        }).toList();

        String currentMonth = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue();
        String nmSeedString = acc.getId() + "-NM-" + currentMonth;
        Random nmRand = new Random(nmSeedString.hashCode());

        List<Skin> nmPool = new ArrayList<>(allSkins);
        Collections.shuffle(nmPool, nmRand);

        List<NightMarketOfferResponse> nightMarket = nmPool.stream().limit(6).map(s -> {
            int originalPrice = (s.getPrice() != null && s.getPrice() > 0) ? s.getPrice() : 1775;
            int discount = nmRand.nextInt(36) + 10;
            int discountedPrice = (int) (originalPrice * (1.0 - (discount / 100.0)));

            return new NightMarketOfferResponse(
                    s.getUuid(), s.getDisplayName(), s.getWeaponType(),
                    1, s.getTotalLevels() != null ? s.getTotalLevels() : 1,
                    s.getDisplayIcon(), s.getVariants(), s.getSwatches(),
                    discountedPrice, originalPrice, discount
            );
        }).toList();

        int vp = nmRand.nextInt(5000) + 500;
        int rp = nmRand.nextInt(300) + 40;
        int kc = nmRand.nextInt(10000) + 2000;

        return new StoreResponse(dailyOffers, nightMarket, vp, rp, kc);

    }
}