package com.valoexplore.backend.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter @Setter
public class BattlepassStats {
    private Integer bpCurrentTier;
    private Integer bpMaxTier;
    private Integer bpXpProgress;
}