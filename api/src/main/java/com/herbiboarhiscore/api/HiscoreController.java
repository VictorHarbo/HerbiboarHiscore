package com.herbiboarhiscore.api;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scores")
public class HiscoreController
{
    private final HiscoreService service;

    public HiscoreController(HiscoreService service)
    {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<HiscoreEntry> submitScore(@Valid @RequestBody ScoreRequest request)
    {
        HiscoreEntry saved = service.upsertScore(request.getPlayerName(), request.getHarvestCount());
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<HiscoreEntry> getScores()
    {
        return service.getScoresRanked();
    }
}
