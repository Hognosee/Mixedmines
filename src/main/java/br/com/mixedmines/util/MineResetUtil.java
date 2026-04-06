package br.com.mixedmines.util;

import br.com.mixedmines.model.Mine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class MineResetUtil {

    private MineResetUtil() {
    }

    public static void resetMine(Mine mine) {
        if (mine == null) return;

        Location pos1 = mine.getPos1();
        Location pos2 = mine.getPos2();

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        int mined = mine.getBrokenBlocks();

        // Teleporta jogadores para cima da mina antes do reset
        double centerX = (minX + maxX) / 2.0D + 0.5D;
        double centerZ = (minZ + maxZ) / 2.0D + 0.5D;
        double safeY = maxY + 2.5D;

        Location safeLocation = new Location(pos1.getWorld(), centerX, safeY, centerZ);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (mine.contains(player.getLocation())) {
                player.teleport(safeLocation);
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Block block = pos1.getWorld().getBlockAt(x, y, z);
                    Material material = mine.getRandomMaterial();
                    block.setType(material);
                }
            }
        }

        mine.resetStats();

        if (!mine.isSilent()) {
            Bukkit.broadcastMessage(
                    "§e§lMIXEDMINES §7» A mina §a" + mine.getName() +
                            " §7foi resetada! §8(" + mined + " blocos minerados)"
            );
        }
    }
}