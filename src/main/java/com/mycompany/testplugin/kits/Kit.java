package com.mycompany.testplugin.kits;

import org.bukkit.Material;

public class Kit {
    private final String id;
    private final Material itemMaterial;
    private final String displayName;
    private final int startingShots;
    private final int refillCount;
    private final int refillCooldownSeconds;
    private final double velocity;
    private final double spreadDegrees;
    private final int pellets;

    public Kit(String id, Material itemMaterial, String displayName,
               int startingShots, int refillCount, int refillCooldownSeconds,
               double velocity, double spreadDegrees, int pellets) {
        this.id = id;
        this.itemMaterial = itemMaterial;
        this.displayName = displayName;
        this.startingShots = startingShots;
        this.refillCount = refillCount;
        this.refillCooldownSeconds = refillCooldownSeconds;
        this.velocity = velocity;
        this.spreadDegrees = spreadDegrees;
        this.pellets = pellets;
    }

    public String getId() { return id; }
    public Material getItemMaterial() { return itemMaterial; }
    public String getDisplayName() { return displayName; }
    public int getStartingShots() { return startingShots; }
    public int getRefillCount() { return refillCount; }
    public int getRefillCooldownSeconds() { return refillCooldownSeconds; }
    public double getVelocity() { return velocity; }
    public double getSpreadDegrees() { return spreadDegrees; }
    public int getPellets() { return pellets; }
}