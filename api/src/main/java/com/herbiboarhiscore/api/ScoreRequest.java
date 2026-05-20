package com.herbiboarhiscore.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ScoreRequest
{
    @NotBlank
    @Size(max = 12)
    @Pattern(regexp = "[A-Za-z0-9 _-]+", message = "Player name contains invalid characters")
    private String playerName;

    @Min(0)
    @Max(1_000_000)
    private int harvestCount;

    public String getPlayerName() { return playerName; }
    public int getHarvestCount() { return harvestCount; }

    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public void setHarvestCount(int harvestCount) { this.harvestCount = harvestCount; }
}
