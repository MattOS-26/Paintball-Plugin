package com.mycompany.testplugin.arena;

import com.mycompany.testplugin.PaintZone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

public class ArenaManager {

    private final PaintZone plugin;
    private final File arenasFile;
    private final YamlConfiguration arenasCfg;
    private final Map<String, Arena> arenas = new HashMap<>();
    private final Map<String, LinkedHashSet<UUID>> queues = new HashMap<>();

    public ArenaManager(PaintZone plugin) {
        this.plugin = plugin;
        this.arenasFile = new File(plugin.getDataFolder(), "arenas.yml");
        this.arenasCfg = YamlConfiguration.loadConfiguration(arenasFile);
        loadArenas();
    }

    private void loadArenas() {
        arenas.clear();
        queues.clear();
        if (!arenasCfg.isConfigurationSection("arenas")) return;
        Set<String> keys = arenasCfg.getConfigurationSection("arenas").getKeys(false);
        for (String key : keys) {
            try {
                Arena a = new Arena(key);
                String base = "arenas." + key + ".";
                if (arenasCfg.isSet(base + "maxPerTeam")) a.setMaxPerTeam(arenasCfg.getInt(base + "maxPerTeam"));
                if (arenasCfg.isSet(base + "redSpawn")) a.setRedSpawn(deserializeLocation(base + "redSpawn"));
                if (arenasCfg.isSet(base + "blueSpawn")) a.setBlueSpawn(deserializeLocation(base + "blueSpawn"));
                if (arenasCfg.isSet(base + "redFlag")) a.setRedFlag(deserializeLocation(base + "redFlag"));
                if (arenasCfg.isSet(base + "blueFlag")) a.setBlueFlag(deserializeLocation(base + "blueFlag"));
                if (arenasCfg.isSet(base + "spectate")) a.setSpectate(deserializeLocation(base + "spectate"));
                arenas.put(key.toLowerCase(), a);
                queues.put(key.toLowerCase(), new LinkedHashSet<>());
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load arena: " + key + " - " + e.getMessage());
            }
        }
        plugin.getLogger().info("Loaded " + arenas.size() + " arenas.");
    }

    public Arena getArena(String id) {
        if (id == null) return null;
        return arenas.get(id.toLowerCase());
    }

    public Collection<Arena> getAllArenas() { return arenas.values(); }

    public boolean createArena(String id) {
        if (getArena(id) != null) return false;
        Arena a = new Arena(id);
        arenas.put(id.toLowerCase(), a);
        queues.put(id.toLowerCase(), new LinkedHashSet<>());
        saveArena(a);
        return true;
    }

    public boolean deleteArena(String id) {
        if (!arenas.containsKey(id.toLowerCase())) return false;
        arenas.remove(id.toLowerCase());
        queues.remove(id.toLowerCase());
        arenasCfg.set("arenas." + id, null);
        saveFile();
        return true;
    }

    public void setRedSpawn(String arenaId, Location loc) {
        Arena a = createIfMissing(arenaId);
        a.setRedSpawn(loc);
        saveArena(a);
    }

    public void setBlueSpawn(String arenaId, Location loc) {
        Arena a = createIfMissing(arenaId);
        a.setBlueSpawn(loc);
        saveArena(a);
    }

    public void setRedFlag(String arenaId, Location loc) {
        Arena a = createIfMissing(arenaId);
        a.setRedFlag(loc);
        saveArena(a);
    }

    public void setBlueFlag(String arenaId, Location loc) {
        Arena a = createIfMissing(arenaId);
        a.setBlueFlag(loc);
        saveArena(a);
    }

    public void setSpectate(String arenaId, Location loc) {
        Arena a = createIfMissing(arenaId);
        a.setSpectate(loc);
        saveArena(a);
    }

    private Arena createIfMissing(String id) {
        if (getArena(id) == null) createArena(id);
        return getArena(id);
    }

    private void saveArena(Arena a) {
        String base = "arenas." + a.getId() + ".";
        arenasCfg.set(base + "maxPerTeam", a.getMaxPerTeam());
        if (a.getRedSpawn() != null) serializeLocation(base + "redSpawn", a.getRedSpawn());
        if (a.getBlueSpawn() != null) serializeLocation(base + "blueSpawn", a.getBlueSpawn());
        if (a.getRedFlag() != null) serializeLocation(base + "redFlag", a.getRedFlag());
        if (a.getBlueFlag() != null) serializeLocation(base + "blueFlag", a.getBlueFlag());
        if (a.getSpectate() != null) serializeLocation(base + "spectate", a.getSpectate());
        saveFile();
    }

    private void serializeLocation(String path, Location loc) {
        if (loc == null) return;
        arenasCfg.set(path + ".world", loc.getWorld().getName());
        arenasCfg.set(path + ".x", loc.getX());
        arenasCfg.set(path + ".y", loc.getY());
        arenasCfg.set(path + ".z", loc.getZ());
        arenasCfg.set(path + ".yaw", loc.getYaw());
        arenasCfg.set(path + ".pitch", loc.getPitch());
    }

    private Location deserializeLocation(String path) {
        String worldName = arenasCfg.getString(path + ".world");
        World w = Bukkit.getWorld(worldName);
        if (w == null) return null;
        double x = arenasCfg.getDouble(path + ".x");
        double y = arenasCfg.getDouble(path + ".y");
        double z = arenasCfg.getDouble(path + ".z");
        float yaw = (float) arenasCfg.getDouble(path + ".yaw");
        float pitch = (float) arenasCfg.getDouble(path + ".pitch");
        return new Location(w, x, y, z, yaw, pitch);
    }

    private void saveFile() {
        try { arenasCfg.save(arenasFile); } catch (Exception e) { plugin.getLogger().severe("Failed to save arenas.yml: " + e.getMessage()); }
    }

    // --- Queue operations (Option A) ---

    /** Add player to arena queue. Returns true if added, false if already in queue or full. */
    public boolean joinQueue(String arenaId, java.util.UUID playerId) {
        Arena a = getArena(arenaId);
        if (a == null) return false;
        LinkedHashSet<UUID> q = queues.computeIfAbsent(arenaId.toLowerCase(), k -> new LinkedHashSet<>());
        int max = a.getMaxPerTeam() * 2;
        if (q.size() >= max) return false;
        return q.add(playerId);
    }

    public boolean leaveQueue(String arenaId, java.util.UUID playerId) {
        LinkedHashSet<UUID> q = queues.get(arenaId.toLowerCase());
        if (q == null) return false;
        return q.remove(playerId);
    }

    public int getQueueSize(String arenaId) {
        LinkedHashSet<UUID> q = queues.get(arenaId.toLowerCase());
        return q == null ? 0 : q.size();
    }

    public void removePlayerFromAllQueues(java.util.UUID playerId) {
        for (LinkedHashSet<UUID> q : queues.values()) q.remove(playerId);
    }
}