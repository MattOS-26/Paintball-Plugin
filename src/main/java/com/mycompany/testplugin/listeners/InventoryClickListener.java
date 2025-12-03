package com.mycompany.testplugin.listeners;

import com.mycompany.testplugin.PaintZone;
import com.mycompany.testplugin.arena.ArenaManager;
import com.mycompany.testplugin.kits.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    private final PaintZone plugin;
    private final KitManager kitManager;
    private final ArenaManager arenaManager;

    public InventoryClickListener(PaintZone plugin) {
        this.plugin = plugin;
        this.kitManager = plugin.getKitManager();
        this.arenaManager = plugin.getArenaManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (title.equals(plugin.getKitGUI().getTitle())) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player p = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            String kitName = clicked.getItemMeta().getDisplayName();
            kitManager.setSelectedKit(p.getUniqueId(), kitName);
            p.closeInventory();
            p.playSound(p.getLocation(), "minecraft:block.wooden_button.click_on", 1f, 1f);
            p.sendMessage("§aKit selected: " + kitName);
            return;
        }

        if (title.equals(plugin.getArenaGUI().getTitle())) {
            event.setCancelled(true);
            if (!(event.getWhoClicked() instanceof Player)) return;
            Player p = (Player) event.getWhoClicked();
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;
            String arenaName = clicked.getItemMeta().getDisplayName();
            boolean joined = arenaManager.joinQueue(arenaName, p.getUniqueId());
            p.closeInventory();
            p.playSound(p.getLocation(), "minecraft:block.wooden_button.click_on", 1f, 1f);
            if (joined) {
                p.sendMessage("§aYou joined arena §f" + arenaName + "§a. Waiting for game start...");
            } else {
                p.sendMessage("§cCouldn't join arena (maybe full or does not exist).");
            }
            return;
        }
    }
}