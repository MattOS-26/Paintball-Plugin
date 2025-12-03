package com.mycompany.testplugin.gui;

import com.mycompany.testplugin.PaintZone;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitGUI {

    private final PaintZone plugin;
    private final String title = "§9Paint Zone Kits";

    public KitGUI(PaintZone plugin) { this.plugin = plugin; }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, title);

        ItemStack paintball = new ItemStack(Material.DIAMOND_HOE);
        ItemStack sniper = new ItemStack(Material.GOLDEN_HOE);
        ItemStack shotgun = new ItemStack(Material.GOLDEN_AXE);

        ItemMeta m1 = paintball.getItemMeta(); m1.setDisplayName("§bPaintball Gun"); paintball.setItemMeta(m1);
        ItemMeta m2 = sniper.getItemMeta(); m2.setDisplayName("§aSniper"); sniper.setItemMeta(m2);
        ItemMeta m3 = shotgun.getItemMeta(); m3.setDisplayName("§eShot Gun"); shotgun.setItemMeta(m3);

        inv.setItem(20, paintball);
        inv.setItem(22, sniper);
        inv.setItem(24, shotgun);

        player.openInventory(inv);
        player.playSound(player.getLocation(), "minecraft:block.wooden_button.click_on", 1f, 1f);
    }

    public String getTitle() { return title; }
}