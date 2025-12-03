package com.mycompany.testplugin.gui;

import com.mycompany.testplugin.PaintZone;
import com.mycompany.testplugin.arena.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ArenaGUI {

    private final PaintZone plugin;
    private final String title = "§bPaint Zone Arenas";

    public ArenaGUI(PaintZone plugin) { this.plugin = plugin; }

    public void open(Player player) {
        int count = plugin.getArenaManager().getAllArenas().size();
        int rows = Math.max(2, (int)Math.ceil(count / 9.0));
        rows = Math.min(rows, 6); // max double chest
        Inventory inv = Bukkit.createInventory(null, rows * 9, title);

        int slot = 0;
        for (Arena a : plugin.getArenaManager().getAllArenas()) {
            ItemStack wool = new ItemStack(Material.WHITE_WOOL);
            ItemMeta meta = wool.getItemMeta();
            meta.setDisplayName(a.getId());
            List<String> lore = new ArrayList<>();
            lore.add("§aClick to join!");
            int playersWaiting = plugin.getArenaManager().getQueueSize(a.getId());
            lore.add("§5" + playersWaiting + "/" + (a.getMaxPerTeam() * 2) + " Players");
            meta.setLore(lore);
            wool.setItemMeta(meta);
            inv.setItem(slot++, wool);
            if (slot >= inv.getSize()) break;
        }

        player.openInventory(inv);
        player.playSound(player.getLocation(), "minecraft:block.wooden_button.click_on", 1f, 1f);
    }

    public String getTitle() { return title; }
}