package br.com.mixedmines.util;

import br.com.mixedmines.MixedMinesPlugin;
import br.com.mixedmines.model.Mine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Resets mines in bounded batches to avoid a single long server-thread freeze.
 * Bukkit world and block mutations remain on the main server thread.
 */
public final class MineResetUtil {

    private static final int DEFAULT_BLOCKS_PER_TICK = 350;

    private MineResetUtil() {
    }

    public static void resetMine(final Mine mine) {
        resetMine(mine, DEFAULT_BLOCKS_PER_TICK);
    }

    public static void resetMine(final Mine mine, int blocksPerTick) {
        if (mine == null || mine.getPos1() == null || mine.getPos2() == null
                || mine.isResetting()) {
            return;
        }

        final Location pos1 = mine.getPos1();
        final Location pos2 = mine.getPos2();
        final World world = pos1.getWorld();
        if (world == null || pos2.getWorld() == null
                || !world.getUID().equals(pos2.getWorld().getUID())) {
            MixedMinesPlugin plugin = MixedMinesPlugin.getInstance();
            if (plugin != null) {
                plugin.getLogger().warning("Reset cancelado: mundo inválido na mina " + mine.getName());
            }
            return;
        }

        final int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        final int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        final int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        final int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        final int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        final int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        final int width = maxX - minX + 1;
        final int height = maxY - minY + 1;
        final int depth = maxZ - minZ + 1;
        final long volume = (long) width * height * depth;
        if (volume <= 0L || volume > Integer.MAX_VALUE) {
            MixedMinesPlugin plugin = MixedMinesPlugin.getInstance();
            if (plugin != null) {
                plugin.getLogger().warning("Reset cancelado: volume inválido na mina " + mine.getName());
            }
            return;
        }

        final int batchSize = Math.max(1, blocksPerTick);
        final List<Player> playersInside = new ArrayList<Player>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (mine.contains(player.getLocation())) {
                playersInside.add(player);
            }
        }

        Location safeLocation = new Location(world,
                (minX + maxX) / 2.0D + 0.5D,
                maxY + 2.5D,
                (minZ + maxZ) / 2.0D + 0.5D);
        for (Player player : playersInside) {
            player.teleport(safeLocation);
        }

        mine.setResetting(true);
        new BukkitRunnable() {
            private long index;

            @Override
            public void run() {
                long end = Math.min(volume, index + batchSize);
                for (; index < end; index++) {
                    int xOffset = (int) (index / ((long) height * depth));
                    int yOffset = (int) ((index / depth) % height);
                    int zOffset = (int) (index % depth);

                    Block block = world.getBlockAt(minX + xOffset, minY + yOffset, minZ + zOffset);
                    Material material = mine.getRandomMaterial();
                    block.setType(material == null ? Material.STONE : material);
                }

                if (index >= volume) {
                    mine.resetStats();
                    mine.setResetting(false);
                    cancel();

                    if (!mine.isSilent()) {
                        Bukkit.broadcastMessage("§e§lMIXEDMINES §7» A mina §a"
                                + mine.getName() + " §7foi resetada!");
                    }
                }
            }
        }.runTaskTimer(MixedMinesPlugin.getInstance(), 1L, 1L);
    }
}
