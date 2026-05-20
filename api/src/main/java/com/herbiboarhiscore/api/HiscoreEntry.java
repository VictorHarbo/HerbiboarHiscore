package com.herbiboarhiscore.api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "hiscores")
public class HiscoreEntry
{
    @Id
    @Column(name = "player_name", length = 12)
    private String playerName;

    @Column(name = "harvest_count", nullable = false)
    private int harvestCount;

    @Column(name = "last_updated", nullable = false)
    private Instant lastUpdated;

    protected HiscoreEntry() {}

    public HiscoreEntry(String playerName, int harvestCount, Instant lastUpdated)
    {
        this.playerName = playerName;
        this.harvestCount = harvestCount;
        this.lastUpdated = lastUpdated;
    }

    public String getPlayerName() { return playerName; }
    public int getHarvestCount() { return harvestCount; }
    public Instant getLastUpdated() { return lastUpdated; }

    public void setHarvestCount(int harvestCount) { this.harvestCount = harvestCount; }
    public void setLastUpdated(Instant lastUpdated) { this.lastUpdated = lastUpdated; }
}
