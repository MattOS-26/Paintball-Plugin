package com.mycompany.testplugin.listeners;

import com.mycompany.testplugin.PaintZone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener {

    private final PaintZone plugin;

    public PlayerDropListener(PaintZone plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        String lobbyWorld = plugin.getConfig().getString("lobby.world", "");
        if (event.getPlayer().getWorld().getName().equals(lobbyWorld)) {
            if (event.getItemDrop() == null) return;
            if (event.getItemDrop().getItemStack() == null) return;
            if (!event.getItemDrop().getItemStack().hasItemMeta()) return;
            String disp = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();
            String kitName = plugin.getItemManager().getKitSelector().getItemMeta().getDisplayName();
            String arenaName = plugin.getItemManager().getArenaSelector().getItemMeta().getDisplayName();
            if (disp.equals(kitName) || disp.equals(arenaName)) {
                event.setCancelled(true);
                event.getPlayer().playSound(event.getPlayer().getLocation(), "minecraft:block.wooden_button.click_on", 0.8f, 1f);
            }
        }
    }
}