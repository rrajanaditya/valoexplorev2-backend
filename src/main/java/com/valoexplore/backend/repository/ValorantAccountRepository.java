package com.valoexplore.backend.repository;

import com.valoexplore.backend.model.ValorantAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ValorantAccountRepository extends JpaRepository<ValorantAccount, Long> {
    @Query("SELECT v FROM ValorantAccount v WHERE v.user.auth0Id = :auth0Id " +
            "AND (:search IS NULL OR LOWER(v.username) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ValorantAccount> findByUserAuth0IdAndSearch(
            @Param("auth0Id") String auth0Id,
            @Param("search") String search,
            Pageable pageable
    );
    Optional<ValorantAccount> findByUserAuth0IdAndUsername(String auth0Id, String username);
}
