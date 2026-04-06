package br.com.mixedmines.manager;

import br.com.mixedmines.MixedMinesPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MinesFileManager {

    private final MixedMinesPlugin plugin;
    private File file;
    private FileConfiguration config;

    public MinesFileManager(MixedMinesPlugin plugin) {
        this.plugin = plugin;
        setup();
    }

    private void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        file = new File(plugin.getDataFolder(), "mines.yml");

        if (!file.exists()) {
            plugin.saveResource("mines.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Não foi possível salvar o mines.yml.");
            e.printStackTrace();
        }
    }
}