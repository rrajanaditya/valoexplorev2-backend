package com.valoexplore.backend.repository;

import com.valoexplore.backend.model.Spray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SprayRepository extends JpaRepository<Spray, Long> {
    @Query("SELECT s FROM ValorantAccount a JOIN a.ownedSprays s WHERE a.user.auth0Id = :auth0Id AND a.username = :username " +
            "AND (:search IS NULL OR :search = '' OR LOWER(s.displayName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Spray> findAccountSpraysPaginated(
            @Param("auth0Id") String auth0Id,
            @Param("username") String username,
            @Param("search") String search,
            Pageable pageable);
}