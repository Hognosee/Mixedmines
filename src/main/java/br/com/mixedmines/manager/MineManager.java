package br.com.mixedmines.manager;

import br.com.mixedmines.model.Mine;
import br.com.mixedmines.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class MineManager {

    private static final Map<String, Mine> mines = new HashMap<String, Mine>();

    public static void addMine(Mine mine) {
        mines.put(mine.getName().toLowerCase(), mine);
    }

    public static Mine getMine(String name) {
        return mines.get(name.toLowerCase());
    }

    public static Map<String, Mine> getMines() {
        return mines;
    }

    public static void loadMines(FileConfiguration config) {
        if (!config.contains("mines")) return;

        ConfigurationSection minesSection = config.getConfigurationSection("mines");
        if (minesSection == null) return;

        for (String mineName : minesSection.getKeys(false)) {

            ConfigurationSection mineSection = minesSection.getConfigurationSection(mineName);
            if (mineSection == null) continue;

            Location pos1 = LocationUtil.loadLocation(mineSection.getConfigurationSection("pos1"));
            Location pos2 = LocationUtil.loadLocation(mineSection.getConfigurationSection("pos2"));

            Mine mine = new Mine(mineName, pos1, pos2);

            int resetPercent = mineSection.getInt("reset-percent", 100);
            mine.setResetPercent(resetPercent);

            int resetDelay = mineSection.getInt("reset-delay", 0);
            mine.setResetDelay(resetDelay);

            boolean silent = mineSection.getBoolean("silent", false);
            mine.setSilent(silent);

            ConfigurationSection compositionSection = mineSection.getConfigurationSection("composition");
            if (compositionSection != null) {
                for (String materialName : compositionSection.getKeys(false)) {
                    Material material = Material.matchMaterial(materialName);

                    if (material != null) {
                        int percent = compositionSection.getInt(materialName);
                        mine.setBlock(material, percent);
                    }
                }
            }

            addMine(mine);
        }
    }
}
