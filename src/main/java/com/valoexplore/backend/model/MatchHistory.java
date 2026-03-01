package com.valoexplore.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "matches")
@Getter @Setter @NoArgsConstructor
public class MatchHistory extends BaseEntity {
    private String agent;
    private String agentName;
    private String map;
    private String result;
    private String score;
    private String kda;
    private Integer combatScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private ValorantAccount account;
}