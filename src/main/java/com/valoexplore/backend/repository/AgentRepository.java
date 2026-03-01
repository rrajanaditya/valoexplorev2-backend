package com.valoexplore.backend.repository;

import com.valoexplore.backend.model.Agent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    @Query("SELECT ag FROM ValorantAccount a JOIN a.ownedAgents ag WHERE a.user.auth0Id = :auth0Id AND a.username = :username " +
            "AND (:search IS NULL OR :search = '' OR LOWER(ag.displayName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Agent> findAccountAgentsPaginated(
            @Param("auth0Id") String auth0Id,
            @Param("username") String username,
            @Param("search") String search,
            Pageable pageable);
}