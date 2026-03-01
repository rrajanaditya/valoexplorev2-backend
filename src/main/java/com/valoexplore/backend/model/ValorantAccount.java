package com.valoexplore.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "valorant_accounts")
@Getter @Setter @NoArgsConstructor
public class ValorantAccount extends BaseEntity {

    private String username;
    private String tagline;
    private Integer level;
    private String title;
    private String country;
    private String region;
    private Boolean emailVerified = false;
    private Boolean phoneVerified = false;
    private LocalDateTime accountCreationDate;
    private String bannerUrl;
    private String avatarUrl;

    @Embedded
    private BattlepassStats battlepass;

    private Integer rr;
    private LocalDateTime lastRankChange;
    private String lastRankChangeString;
    private Double winRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "highest_rank_id")
    private CompetitiveRank highestRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_rank_id")
    private CompetitiveRank currentRank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchHistory> matches = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountAlias> previousAliases = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountBan> bans = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountSeasonRank> seasonRanks = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "account_skins", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "skin_id"))
    private List<Skin> ownedSkins = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "account_cards", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<PlayerCard> ownedCards = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "account_buddies", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "buddy_id"))
    private List<Buddy> ownedBuddies = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "account_sprays", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "spray_id"))
    private List<Spray> ownedSprays = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "account_agents", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "agent_id"))
    private List<Agent> ownedAgents = new ArrayList<>();
}