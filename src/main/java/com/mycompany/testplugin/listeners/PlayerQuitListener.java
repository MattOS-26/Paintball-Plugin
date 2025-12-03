package com.mycompany.testplugin.listeners;

import com.mycompany.testplugin.PaintZone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final PaintZone plugin;

    public PlayerQuitListener(PaintZone plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Step 1 implementation: make sure player leaving is cleaned up from any
        // in-memory structures. At this stage we don't have a full Game class,
        // so just provide a placeholder cleanup call for when Game tracking is added.
        // TODO: remove player from any arena/game and handle flag returns if necessary.
        // For now, simply log:
        plugin.getLogger().info("Player quit: " + event.getPlayer().getName());
    }
}