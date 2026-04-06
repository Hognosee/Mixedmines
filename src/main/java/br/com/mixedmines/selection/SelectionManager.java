package br.com.mixedmines.selection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SelectionManager {

    private static final HashMap<UUID, Location> pos1 = new HashMap<>();
    private static final HashMap<UUID, Location> pos2 = new HashMap<>();

    public static void setPos1(Player player, Location location) {
        pos1.put(player.getUniqueId(), location);
    }

    public static void setPos2(Player player, Location location) {
        pos2.put(player.getUniqueId(), location);
    }

    public static Location getPos1(Player player) {
        return pos1.get(player.getUniqueId());
    }

    public static Location getPos2(Player player) {
        return pos2.get(player.getUniqueId());
    }

}