package com.valoexplore.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "player_cards")
@Getter @Setter @NoArgsConstructor
public class PlayerCard extends BaseEntity {
    private String uuid;
    private String displayName;
    private String largeArt;
    private String wideArt;
    private Integer price;
}