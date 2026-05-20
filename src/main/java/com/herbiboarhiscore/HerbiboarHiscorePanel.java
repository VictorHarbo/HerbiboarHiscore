package com.herbiboarhiscore;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.PluginPanel;

public class HerbiboarHiscorePanel extends PluginPanel
{
    private final HerbiboarHiscoreClient client;
    private final JPanel listPanel;

    HerbiboarHiscorePanel(HerbiboarHiscoreClient client)
    {
        super(false);
        this.client = client;

        setLayout(new BorderLayout(0, 8));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(ColorScheme.DARK_GRAY_COLOR);

        // Title
        JLabel title = new JLabel("Herbiboar Hiscore", SwingConstants.CENTER);
        title.setFont(FontManager.getRunescapeBoldFont().deriveFont(Font.BOLD, 16f));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 0, 6, 0));
        add(title, BorderLayout.NORTH);

        // Scores list
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(ColorScheme.DARKER_GRAY_COLOR);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> refresh());
        add(refreshButton, BorderLayout.SOUTH);
    }

    public void refresh()
    {
        client.getScoresAsync(scores -> SwingUtilities.invokeLater(() -> updateList(scores)));
    }

    private void updateList(List<ScoreEntry> scores)
    {
        listPanel.removeAll();

        if (scores.isEmpty())
        {
            JLabel empty = new JLabel("No scores yet.", SwingConstants.CENTER);
            empty.setForeground(Color.GRAY);
            empty.setBorder(new EmptyBorder(10, 0, 0, 0));
            listPanel.add(empty);
        }
        else
        {
            for (int i = 0; i < scores.size(); i++)
            {
                ScoreEntry entry = scores.get(i);
                String text = String.format("%d.  %s  —  %,d", i + 1, entry.getPlayerName(), entry.getHarvestCount());
                JLabel label = new JLabel(text);
                label.setForeground(i == 0 ? new Color(0xFFD700) : Color.WHITE);
                label.setBorder(new EmptyBorder(5, 8, 5, 8));
                listPanel.add(label);

                if (i < scores.size() - 1)
                {
                    JSeparator sep = new JSeparator();
                    sep.setForeground(ColorScheme.DARK_GRAY_COLOR);
                    listPanel.add(sep);
                }
            }
        }

        listPanel.revalidate();
        listPanel.repaint();
    }
}
