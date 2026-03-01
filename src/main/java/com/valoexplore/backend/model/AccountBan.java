package com.valoexplore.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_bans")
@Getter @Setter @NoArgsConstructor
public class AccountBan extends BaseEntity {
    private String type;
    private String reason;
    private LocalDateTime expiry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private ValorantAccount account;
}