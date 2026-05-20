package com.herbiboarhiscore.api;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HiscoreService
{
    private final HiscoreRepository repository;

    public HiscoreService(HiscoreRepository repository)
    {
        this.repository = repository;
    }

    @Transactional
    public HiscoreEntry upsertScore(String playerName, int harvestCount)
    {
        HiscoreEntry entry = repository.findById(playerName)
            .orElseGet(() -> new HiscoreEntry(playerName, 0, Instant.now()));

        entry.setHarvestCount(harvestCount);
        entry.setLastUpdated(Instant.now());
        return repository.save(entry);
    }

    public List<HiscoreEntry> getScoresRanked()
    {
        return repository.findAllByOrderByHarvestCountDesc();
    }
}
