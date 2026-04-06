package br.com.mixedmines.placeholder;

import br.com.mixedmines.MixedMinesPlugin;
import br.com.mixedmines.manager.MineManager;
import br.com.mixedmines.model.Mine;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

import java.util.Locale;

public class MixedMinesPlaceholder extends PlaceholderExpansion {

    private final MixedMinesPlugin plugin;

    public MixedMinesPlaceholder(MixedMinesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return "mixedmines";
    }

    @Override
    public String getAuthor() {
        return "Hognose";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        String[] split = params.split("_", 2);
        if (split.length < 2) {
            return "";
        }

        String mineName = split[0];
        String key = split[1].toLowerCase(Locale.ROOT);

        Mine mine = MineManager.getMine(mineName);
        if (mine == null) {
            return "";
        }

        if (key.equals("broken")) {
            return String.valueOf(mine.getBrokenBlocks());
        }

        if (key.equals("percentage")) {
            return String.format(Locale.US, "%.2f", mine.getBrokenPercentage());
        }

        if (key.equals("volume")) {
            return String.valueOf(mine.getVolume());
        }

        if (key.equals("reset_percent")) {
            return String.valueOf(mine.getResetPercent());
        }

        if (key.equals("reset_delay")) {
            return String.valueOf(mine.getResetDelay());
        }

        if (key.equals("silent")) {
            return String.valueOf(mine.isSilent());
        }

        if (key.equals("composition_total")) {
            return String.valueOf(mine.getTotalCompositionPercent());
        }

        if (key.equals("width")) {
            return String.valueOf(mine.getWidth());
        }

        if (key.equals("height")) {
            return String.valueOf(mine.getHeight());
        }

        if (key.equals("depth")) {
            return String.valueOf(mine.getDepth());
        }

        if (key.equals("world")) {
            return mine.getPos1().getWorld().getName();
        }

        return "";
    }
}