package com.mycompany.testplugin;

import com.mycompany.testplugin.arena.ArenaManager;
import com.mycompany.testplugin.commands.PZCommand;
import com.mycompany.testplugin.gui.ArenaGUI;
import com.mycompany.testplugin.gui.KitGUI;
import com.mycompany.testplugin.kits.KitManager;
import com.mycompany.testplugin.listeners.InventoryClickListener;
import com.mycompany.testplugin.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class PaintZone extends JavaPlugin {

    private static PaintZone instance;
    private ArenaManager arenaManager;
    private KitManager kitManager;
    private ItemManager itemManager;
    private KitGUI kitGUI;
    private ArenaGUI arenaGUI;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        saveResource("kits.yml", false);
        saveResource("arenas.yml", false);

        // managers and GUIs
        kitManager = new KitManager(this);
        arenaManager = new ArenaManager(this);
        itemManager = new ItemManager(this);
        kitGUI = new KitGUI(this);
        arenaGUI = new ArenaGUI(this);

        // commands
        if (getCommand("pz") != null) getCommand("pz").setExecutor(new PZCommand(this));

        // listeners
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);

        getLogger().info("PaintZone enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("PaintZone disabled.");
    }

    public static PaintZone get() { return instance; }

    public ArenaManager getArenaManager() { return arenaManager; }
    public KitManager getKitManager() { return kitManager; }
    public ItemManager getItemManager() { return itemManager; }
    public KitGUI getKitGUI() { return kitGUI; }
    public ArenaGUI getArenaGUI() { return arenaGUI; }

    /** Returns the lobby world name stored in config, or null if not set. */
    public String getLobbyWorldName() {
        return getConfig().getString("lobby.world", null);
    }

    /** Returns the lobby location from config or null if missing. */
    public Location getLobbyLocation() {
        String world = getConfig().getString("lobby.world", null);
        if (world == null) return null;
        if (Bukkit.getWorld(world) == null) return null;
        double x = getConfig().getDouble("lobby.x");
        double y = getConfig().getDouble("lobby.y");
        double z = getConfig().getDouble("lobby.z");
        float yaw = (float) getConfig().getDouble("lobby.yaw");
        float pitch = (float) getConfig().getDouble("lobby.pitch");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}