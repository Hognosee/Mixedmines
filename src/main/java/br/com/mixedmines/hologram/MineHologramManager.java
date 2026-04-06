package br.com.mixedmines.hologram;

import br.com.mixedmines.MixedMinesPlugin;
import br.com.mixedmines.model.Mine;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class MineHologramManager {

    private static final Map<String, Hologram> holograms = new HashMap<String, Hologram>();

    public static void createHologram(Mine mine) {
        if (mine == null || mine.getPos1() == null) return;

        Location loc = mine.getPos1().clone().add(0.5, 3.0, 0.5);

        Hologram old = holograms.get(mine.getName());
        if (old != null) {
            old.delete();
        }

        Hologram hologram = HologramsAPI.createHologram(MixedMinesPlugin.getInstance(), loc);
        holograms.put(mine.getName(), hologram);

        updateHologram(mine);
    }

    public static void updateHologram(Mine mine) {
        Hologram hologram = holograms.get(mine.getName());
        if (hologram == null) return;

        hologram.clearLines();

        hologram.appendTextLine("§b§lMina " + mine.getName());
        hologram.appendTextLine("§7Quebrados: §e" + String.format("%.2f", mine.getBrokenPercentage()) + "%");
        hologram.appendTextLine("§7Reseta com §c" + mine.getResetPercent() + "%");
    }

    public static void deleteHologram(String mineName) {
        Hologram hologram = holograms.remove(mineName);
        if (hologram != null) {
            hologram.delete();
        }
    }
}