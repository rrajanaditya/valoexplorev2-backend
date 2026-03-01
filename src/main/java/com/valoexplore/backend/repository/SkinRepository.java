package com.valoexplore.backend.repository;

import com.valoexplore.backend.model.Skin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkinRepository extends JpaRepository<Skin, Long> {
    @Query(value = "SELECT * FROM skin ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Skin> findRandomSkins(int count);

    @Query(
            value = "SELECT s FROM Skin s, ValorantAccount a " +
                    "WHERE s MEMBER OF a.ownedSkins " +
                    "AND a.user.auth0Id = :auth0Id " +
                    "AND a.username = :username " +
                    "AND (:search IS NULL OR LOWER(s.displayName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
                    "AND (:filterCode = 'ALL' OR s.source = :filterCode)",
            countQuery = "SELECT count(s) FROM Skin s, ValorantAccount a " +
                    "WHERE s MEMBER OF a.ownedSkins " +
                    "AND a.user.auth0Id = :auth0Id " +
                    "AND a.username = :username " +
                    "AND (:search IS NULL OR LOWER(s.displayName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
                    "AND (:filterCode = 'ALL' OR s.source = :filterCode)"
    )
    Page<Skin> findAccountSkinsPaginated(
            @Param("auth0Id") String auth0Id,
            @Param("username") String username,
            @Param("search") String search,
            @Param("filterCode") String filterCode,
            Pageable pageable
    );
    @Query("SELECT COALESCE(SUM(s.price), 0) FROM ValorantAccount a JOIN a.ownedSkins s WHERE a.user.auth0Id = :auth0Id AND a.username = :username")
    Long getTotalVpSpent(@Param("auth0Id") String auth0Id, @Param("username") String username);
}