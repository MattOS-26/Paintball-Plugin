package com.mycompany.testplugin.player;

import com.mycompany.testplugin.kits.Kit;
import org.bukkit.entity.Player;

public class PZPlayer {
    private final Player player;
    private Kit kit;
    private int remainingShots;
    private long lastRefillTimestamp = 0;
    private boolean carryingFlag = false;
    private String team; // "red" or "blue"

    public PZPlayer(Player p) {
        this.player = p;
    }

    public Player getPlayer() { return player; }
    // getters/setters for kit, shots, flag, team
}