package com.valoexplore.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skin")
@Getter @Setter @NoArgsConstructor
public class Skin extends BaseEntity {
    private String uuid;
    private String displayName;
    private String displayIcon;
    private String tierName;
    private String weaponType;

    private Integer price;
    private String source;
    private Integer totalLevels;

    @ElementCollection
    @CollectionTable(name = "skin_variants", joinColumns = @JoinColumn(name = "skin_id"))
    @Column(name = "variant_url")
    private List<String> variants = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "skin_swatches", joinColumns = @JoinColumn(name = "skin_id"))
    @Column(name = "swatch_url")
    private List<String> swatches = new ArrayList<>();
}