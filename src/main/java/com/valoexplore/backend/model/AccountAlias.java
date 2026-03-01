package com.valoexplore.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_aliases")
@Getter @Setter @NoArgsConstructor
public class AccountAlias extends BaseEntity {
    private String gameName;
    private String tagLine;
    private LocalDateTime aliasCreatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private ValorantAccount account;
}