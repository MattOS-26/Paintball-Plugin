package com.mycompany.testplugin;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemManager {

    private final PaintZone plugin;
    private ItemStack kitSelector;
    private ItemStack arenaSelector;

    public ItemManager(PaintZone plugin) {
        this.plugin = plugin;
        loadItems();
    }

    /**
     * Loads selector items from config with robust fallbacks.
     */
    public void loadItems() {
        FileConfiguration cfg = plugin.getConfig();

        // KIT SELECTOR: check items.kitSelector.*, fallback to items.kit.* and old keys
        String kitMat = firstNonEmpty(
                cfg.getString("items.kitSelector.material"),
                cfg.getString("items.kit.material"),
                cfg.getString("items.kitSelector.material"), // try again if older naming
                "DIAMOND_SWORD"
        ).toUpperCase();

        String kitName = firstNonEmpty(
                cfg.getString("items.kitSelector.name"),
                cfg.getString("items.kit.name"),
                cfg.getString("items.kitSelector.name"),
                "§bKit Selector"
        );

        // ARENA SELECTOR: check items.arenaSelector.*, fallback to items.join.*
        String arenaMat = firstNonEmpty(
                cfg.getString("items.arenaSelector.material"),
                cfg.getString("items.join.material"),
                "COMPASS"
        ).toUpperCase();

        String arenaName = firstNonEmpty(
                cfg.getString("items.arenaSelector.name"),
                cfg.getString("items.join.name"),
                "§aArena Selector"
        );

        kitSelector = buildItem(kitMat, kitName);
        arenaSelector = buildItem(arenaMat, arenaName);
    }

    private String firstNonEmpty(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return v;
        }
        return "";
    }

    private ItemStack buildItem(String matName, String displayName) {
        Material mat;
        try {
            mat = Material.valueOf(matName.toUpperCase());
        } catch (Exception e) {
            // fallback to common defaults
            mat = Material.COMPASS;
        }
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            item.setItemMeta(meta);
        }
        return item;
    }

    public ItemStack getKitSelector() {
        return kitSelector == null ? new ItemStack(Material.DIAMOND_SWORD) : kitSelector.clone();
    }

    public ItemStack getArenaSelector() {
        return arenaSelector == null ? new ItemStack(Material.COMPASS) : arenaSelector.clone();
    }
}