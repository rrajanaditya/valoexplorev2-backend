package com.valoexplore.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "competitive_ranks")
@Getter
@Setter
@NoArgsConstructor
public class CompetitiveRank extends BaseEntity {
    private Integer tier;
    private String tierName;
    private String largeIcon;
}