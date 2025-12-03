package com.mycompany.testplugin.arena;

import org.bukkit.Location;

public class Arena {
    private final String id;
    private Location redSpawn;
    private Location blueSpawn;
    private Location redFlag;
    private Location blueFlag;
    private Location spectate;
    private int maxPerTeam = 10;

    public Arena(String id) { this.id = id; }

    public String getId() { return id; }
    public Location getRedSpawn() { return redSpawn; }
    public void setRedSpawn(Location redSpawn) { this.redSpawn = redSpawn; }
    public Location getBlueSpawn() { return blueSpawn; }
    public void setBlueSpawn(Location blueSpawn) { this.blueSpawn = blueSpawn; }
    public Location getRedFlag() { return redFlag; }
    public void setRedFlag(Location redFlag) { this.redFlag = redFlag; }
    public Location getBlueFlag() { return blueFlag; }
    public void setBlueFlag(Location blueFlag) { this.blueFlag = blueFlag; }
    public Location getSpectate() { return spectate; }
    public void setSpectate(Location spectate) { this.spectate = spectate; }
    public int getMaxPerTeam() { return maxPerTeam; }
    public void setMaxPerTeam(int maxPerTeam) { this.maxPerTeam = maxPerTeam; }
}