package br.com.mixedmines.listener;

import br.com.mixedmines.MixedMinesPlugin;
import br.com.mixedmines.manager.MineManager;
import br.com.mixedmines.model.Mine;
import br.com.mixedmines.util.MineResetUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class MineBlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        if (event.getBlock().getType() == Material.AIR) return;

        for (final Mine mine : MineManager.getMines().values()) {
            if (mine.contains(event.getBlock().getLocation())) {
                mine.addBrokenBlock();

                if (!mine.isResetting() && mine.getBrokenPercentage() >= mine.getResetPercent()) {
                    mine.setResetting(true);

                    if (mine.getResetDelay() > 0) {

                        if (!mine.isSilent()) {
                            Bukkit.broadcastMessage(
                                    "§e§lMIXEDMINES §7» A mina §a" + mine.getName() +
                                            " §7irá resetar em §e" + mine.getResetDelay() + "s§7."
                            );
                        }

                        Bukkit.getScheduler().runTaskLater(
                                MixedMinesPlugin.getInstance(),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        MineResetUtil.resetMine(mine);
                                    }
                                },
                                mine.getResetDelay() * 20L
                        );
                    } else {
                        Bukkit.getScheduler().runTask(
                                MixedMinesPlugin.getInstance(),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        MineResetUtil.resetMine(mine);
                                    }
                                }
                        );
                    }
                }

                break;
            }
        }
    }
}
