package com.mycompany.testplugin.listeners;

import com.mycompany.testplugin.PaintZone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorldListener implements Listener {

    private final PaintZone plugin;

    public PlayerChangedWorldListener(PaintZone plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player p = event.getPlayer();
        String lobbyWorld = plugin.getConfig().getString("lobby.world", "");
        if (lobbyWorld == null || lobbyWorld.isEmpty()) return;

        // Entered lobby -> give items (slot 0 and 1)
        if (p.getWorld().getName().equals(lobbyWorld)) {
            p.getInventory().setItem(0, plugin.getItemManager().getArenaSelector());
            p.getInventory().setItem(1, plugin.getItemManager().getKitSelector());
            // lock items will be enforced by InventoryClickListener and PlayerDropListener
        } else {
            // left lobby -> ensure selectors removed (they should be removed when game starts)
            if (p.getInventory().getItem(0) != null
                    && p.getInventory().getItem(0).hasItemMeta()
                    && p.getInventory().getItem(0).getItemMeta().getDisplayName().equals(plugin.getItemManager().getArenaSelector().getItemMeta().getDisplayName())) {
                p.getInventory().setItem(0, null);
            }
            if (p.getInventory().getItem(1) != null
                    && p.getInventory().getItem(1).hasItemMeta()
                    && p.getInventory().getItem(1).getItemMeta().getDisplayName().equals(plugin.getItemManager().getKitSelector().getItemMeta().getDisplayName())) {
                p.getInventory().setItem(1, null);
            }
        }
    }
}