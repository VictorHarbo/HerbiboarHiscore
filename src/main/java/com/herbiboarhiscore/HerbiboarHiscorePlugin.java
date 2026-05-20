package com.herbiboarhiscore;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;

@Slf4j
@PluginDescriptor(
	name = "Herbiboar Hiscore",
	description = "Track your Herbiboar hunting high scores",
	tags = {"herbiboar", "hunter", "hiscore"}
)
public class HerbiboarHiscorePlugin extends Plugin
{
	private static final Pattern HARVEST_COUNT_PATTERN =
		Pattern.compile("Your herbiboar harvest count is: (\\d+)");

	private static final Set<Integer> FOSSIL_ISLAND_REGIONS = ImmutableSet.of(
		14650, 14651, 14652, 14906, 14907, 14908, 14909, 15162, 15163, 15164, 15165
	);

	@Inject
	private Client client;

	@Inject
	private HerbiboarHiscoreConfig config;

	@Inject
	private ConfigManager configManager;

	@Inject
	private HerbiboarHiscoreClient hiscoreClient;

	@Inject
	private ClientToolbar clientToolbar;

	private HerbiboarHiscorePanel panel;
	private NavigationButton navButton;

	@Override
	protected void startUp() throws Exception
	{
		panel = new HerbiboarHiscorePanel(hiscoreClient);

		BufferedImage icon = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = icon.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(0x5CBF3A));
		g.fillOval(0, 0, 15, 15);
		g.dispose();

		navButton = NavigationButton.builder()
			.tooltip("Herbiboar Hiscore")
			.icon(icon)
			.priority(5)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
		panel.refresh();
		log.info("Herbiboar Hiscore started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientToolbar.removeNavigation(navButton);
		panel = null;
		navButton = null;
		log.info("Herbiboar Hiscore stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() != ChatMessageType.GAMEMESSAGE
			&& chatMessage.getType() != ChatMessageType.SPAM)
		{
			return;
		}

		if (!isOnFossilIsland())
		{
			return;
		}

		Matcher matcher = HARVEST_COUNT_PATTERN.matcher(chatMessage.getMessage());
		if (matcher.find())
		{
			int harvestCount = Integer.parseInt(matcher.group(1));
			String playerName = client.getLocalPlayer().getName();

			configManager.setConfiguration("herbiboarhiscore", "harvestCount", harvestCount);
			hiscoreClient.submitScore(playerName, harvestCount);
			panel.refresh();
			log.info("Herbiboar harvest count updated to {} for player {}", harvestCount, playerName);
		}
	}

	private boolean isOnFossilIsland()
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return false;
		}

		WorldPoint location = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
		return FOSSIL_ISLAND_REGIONS.contains(location.getRegionID());
	}

	@Provides
	HerbiboarHiscoreConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(HerbiboarHiscoreConfig.class);
	}
}
