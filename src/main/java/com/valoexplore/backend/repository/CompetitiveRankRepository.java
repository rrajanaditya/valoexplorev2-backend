package com.valoexplore.backend.repository;

import com.valoexplore.backend.model.CompetitiveRank;
import com.valoexplore.backend.model.PlayerCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompetitiveRankRepository extends JpaRepository<CompetitiveRank, Long> {
    @Query(value = "SELECT * FROM competitive_ranks ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<CompetitiveRank> findRandomRank();

    @Query(value = "SELECT * FROM competitive_ranks ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<CompetitiveRank> findRandomRanks(int count);
}