package br.com.mixedmines;

import br.com.mixedmines.command.MixedMinesCommand;
import br.com.mixedmines.command.MixedMinesTabCompleter;
import br.com.mixedmines.listener.MineBlockBreakListener;
import br.com.mixedmines.listener.SelectionListener;
import br.com.mixedmines.manager.MineManager;
import br.com.mixedmines.manager.MinesFileManager;
import br.com.mixedmines.placeholder.MixedMinesPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MixedMinesPlugin extends JavaPlugin {

    private static MixedMinesPlugin instance;
    private MinesFileManager minesFileManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        this.minesFileManager = new MinesFileManager(this);

        MineManager.loadMines(minesFileManager.getConfig());

        MixedMinesCommand commandExecutor = new MixedMinesCommand(this);
        MixedMinesTabCompleter tabCompleter = new MixedMinesTabCompleter(this);

        getCommand("mixedmines").setExecutor(commandExecutor);
        getCommand("mixedmines").setTabCompleter(tabCompleter);

        getServer().getPluginManager().registerEvents(new MineBlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new SelectionListener(), this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new MixedMinesPlaceholder(this).register();
            getLogger().info("Hook com PlaceholderAPI habilitado.");
        } else {
            getLogger().warning("PlaceholderAPI não encontrada. Placeholders desativadas.");
        }

        getLogger().info("MixedMines habilitado com sucesso.");
    }

    @Override
    public void onDisable() {
        getLogger().info("MixedMines desabilitado.");
    }

    public void reloadPlugin() {
        reloadConfig();
        minesFileManager.reload();
        MineManager.getMines().clear();
        MineManager.loadMines(minesFileManager.getConfig());
    }

    public static MixedMinesPlugin getInstance() {
        return instance;
    }

    public MinesFileManager getMinesFileManager() {
        return minesFileManager;
    }
}