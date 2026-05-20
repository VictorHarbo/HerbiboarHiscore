package com.herbiboarhiscore;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("herbiboarhiscore")
public interface HerbiboarHiscoreConfig extends Config
{
	@ConfigItem(
		keyName = "apiBaseUrl",
		name = "API Base URL",
		description = "Base URL of the Herbiboar Hiscore API"
	)
	default String apiBaseUrl()
	{
		return "https://your-api-host.com";
	}

	@ConfigItem(
		keyName = "harvestCount",
		name = "Harvest Count",
		description = "Your tracked Herbiboar harvest count",
		hidden = true
	)
	default int harvestCount()
	{
		return 0;
	}
}
