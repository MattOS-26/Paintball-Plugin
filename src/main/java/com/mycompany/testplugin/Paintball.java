package com.mycompany.testplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Paintball extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Paintball plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Paintball plugin disabled!");
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        // Only trigger for main hand (to avoid double firing due to offhand)
        if (event.getHand() != EquipmentSlot.HAND) return;

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) return;

        // Check if it's a diamond hoe named "Paintball Gun"
        if (item.getType() == Material.DIAMOND_HOE && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equalsIgnoreCase("Paintball Gun")) {

                // 1) Check if player has a snowball in their inventory
                if (!player.getInventory().contains(Material.SNOWBALL)) {
                    player.sendMessage("§cYou are out of paintballs!");
                    return;
                }

                // 2) Launch the snowball
                Snowball snowball = player.launchProjectile(Snowball.class);
                Vector direction = player.getLocation().getDirection().multiply(1.5);
                snowball.setVelocity(direction);

                // 3) Play sound when fired
                player.playSound(player.getLocation(), "minecraft:block.note_block.snare", 1.0f, 1.0f);

                // 4) Remove ONE snowball from inventory
                player.getInventory().removeItem(new ItemStack(Material.SNOWBALL, 1));
    
            }
        }
    }
}
