package com.mycompany.testplugin.listeners;

import com.mycompany.testplugin.PaintZone;
import com.mycompany.testplugin.gui.ArenaGUI;
import com.mycompany.testplugin.gui.KitGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

    private final PaintZone plugin;
    private final KitGUI kitGUI;
    private final ArenaGUI arenaGUI;

    public PlayerInteractListener(PaintZone plugin) {
        this.plugin = plugin;
        this.kitGUI = plugin.getKitGUI();
        this.arenaGUI = plugin.getArenaGUI();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // only main hand and right-click
        if (event.getHand() != EquipmentSlot.HAND) return;
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
            case RIGHT_CLICK_BLOCK:
                break;
            default:
                return;
        }

        if (!(event.getPlayer() instanceof Player)) return;
        Player p = event.getPlayer();

        // ensure lobby is set and player is in lobby world
        String lobby = plugin.getLobbyWorldName();
        if (lobby == null || p.getWorld() == null || !p.getWorld().getName().equals(lobby)) return;

        ItemStack hand = p.getInventory().getItemInMainHand();
        if (hand == null) return;

        // Desired display names from ItemManager
        ItemStack cfgKit = plugin.getItemManager().getKitSelector();
        ItemStack cfgArena = plugin.getItemManager().getArenaSelector();

        String kitName = cfgKit.hasItemMeta() && cfgKit.getItemMeta().hasDisplayName()
                ? cfgKit.getItemMeta().getDisplayName() : null;

        String arenaName = cfgArena.hasItemMeta() && cfgArena.getItemMeta().hasDisplayName()
                ? cfgArena.getItemMeta().getDisplayName() : null;

        // First attempt: match by display name
        if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
            String handName = hand.getItemMeta().getDisplayName();
            if (kitName != null && kitName.equals(handName)) {
                kitGUI.open(p);
                event.setCancelled(true);
                return;
            }
            if (arenaName != null && arenaName.equals(handName)) {
                arenaGUI.open(p);
                event.setCancelled(true);
                return;
            }
        }

        // Fallback: match by material (in case display names were missing or stripped)
        Material handMat = hand.getType();
        if (cfgKit != null && handMat == cfgKit.getType()) {
            kitGUI.open(p);
            event.setCancelled(true);
            return;
        }
        if (cfgArena != null && handMat == cfgArena.getType()) {
            arenaGUI.open(p);
            event.setCancelled(true);
        }
    }
}