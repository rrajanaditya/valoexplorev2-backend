package com.valoexplore.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "agents")
@Getter @Setter @NoArgsConstructor
public class Agent extends BaseEntity {
    private String uuid;
    private String displayName;
    private String displayIcon;
    private Integer price;
}