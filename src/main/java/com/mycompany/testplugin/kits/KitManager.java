package com.mycompany.testplugin.kits;

import com.mycompany.testplugin.PaintZone;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KitManager {

    private final PaintZone plugin;
    private final Map<UUID, String> selected = new HashMap<>();

    public KitManager(PaintZone plugin) {
        this.plugin = plugin;
    }

    public void setSelectedKit(UUID player, String kitId) {
        if (kitId == null) selected.remove(player);
        else selected.put(player, kitId);
    }

    public String getSelectedKit(UUID player) {
        return selected.get(player);
    }

    public void clearSelected(UUID player) {
        selected.remove(player);
    }
}