package com.herbiboarhiscore.api;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HiscoreRepository extends JpaRepository<HiscoreEntry, String>
{
    List<HiscoreEntry> findAllByOrderByHarvestCountDesc();
}
