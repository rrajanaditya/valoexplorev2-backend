package com.valoexplore.backend.repository;

import com.valoexplore.backend.model.Buddy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuddyRepository extends JpaRepository<Buddy, Long> {
    @Query("SELECT b FROM ValorantAccount a JOIN a.ownedBuddies b WHERE a.user.auth0Id = :auth0Id AND a.username = :username " +
            "AND (:search IS NULL OR :search = '' OR LOWER(b.displayName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Buddy> findAccountBuddiesPaginated(
            @Param("auth0Id") String auth0Id,
            @Param("username") String username,
            @Param("search") String search,
            Pageable pageable);
}