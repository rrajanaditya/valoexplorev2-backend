package com.valoexplore.backend.repository;

import com.valoexplore.backend.model.PlayerCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerCardRepository extends JpaRepository<PlayerCard, Long> {
    @Query(value = "SELECT * FROM player_cards ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<PlayerCard> findRandomCard();

    @Query(value = "SELECT * FROM player_cards ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<PlayerCard> findRandomCards(int count);

    @Query("SELECT c FROM ValorantAccount a JOIN a.ownedCards c WHERE a.user.auth0Id = :auth0Id AND a.username = :username " +
            "AND (:search IS NULL OR :search = '' OR LOWER(c.displayName) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<PlayerCard> findAccountCardsPaginated(
            @Param("auth0Id") String auth0Id,
            @Param("username") String username,
            @Param("search") String search,
            Pageable pageable);
}