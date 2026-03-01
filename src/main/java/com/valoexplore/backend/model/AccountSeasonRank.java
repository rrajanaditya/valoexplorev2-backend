package com.valoexplore.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "account_season_ranks")
@Getter @Setter @NoArgsConstructor
public class AccountSeasonRank extends BaseEntity {
    private String season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id")
    private CompetitiveRank rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private ValorantAccount account;
}