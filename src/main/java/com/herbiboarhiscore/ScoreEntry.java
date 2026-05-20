package com.herbiboarhiscore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreEntry
{
    private String playerName;
    private int harvestCount;
}
