package com.valoexplore.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @Builder @AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String auth0Id;

    @Column(unique = true, nullable = false)
    private String email;

    private String displayName;
    private String avatarUrl;

    private String plan = "FREE";
    private Integer generationsLeft = 5;
    private Integer maxGenerations = 5;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ValorantAccount> accounts;
}