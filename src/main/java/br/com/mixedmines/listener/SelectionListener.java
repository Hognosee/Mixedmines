package br.com.mixedmines.listener;

import br.com.mixedmines.selection.SelectionManager;
import br.com.mixedmines.util.WandUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SelectionListener implements Listener {

    @EventHandler
    public void onWandUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack item = player.getItemInHand();
        if (!WandUtil.isMineWand(item)) return;
        if (event.getClickedBlock() == null) return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            SelectionManager.setPos1(player, event.getClickedBlock().getLocation());
            player.sendMessage("§e§lMIXEDMINES §7» §aPosição 1 definida.");
            event.setCancelled(true);
            return;
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            SelectionManager.setPos2(player, event.getClickedBlock().getLocation());
            player.sendMessage("§e§lMIXEDMINES §7» §aPosição 2 definida.");
            event.setCancelled(true);
        }
    }
}