package br.com.mixedmines.model;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mine {

    private final String name;
    private Location pos1;
    private Location pos2;

    private final Map<Material, Integer> composition = new HashMap<Material, Integer>();
    private final List<Material> cachedBlocks = new ArrayList<Material>();
    private final Random random = new Random();

    private int brokenBlocks = 0;
    private int resetPercent = 100;
    private int resetDelay = 0;
    private boolean resetting = false;
    private boolean silent = false;

    public Mine(String name, Location pos1, Location pos2) {
        this.name = name;
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public String getName() {
        return name;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public int getWidth() {
        return Math.abs(pos1.getBlockX() - pos2.getBlockX()) + 1;
    }

    public int getHeight() {
        return Math.abs(pos1.getBlockY() - pos2.getBlockY()) + 1;
    }

    public int getDepth() {
        return Math.abs(pos1.getBlockZ() - pos2.getBlockZ()) + 1;
    }

    public int getVolume() {
        return getWidth() * getHeight() * getDepth();
    }

    public Map<Material, Integer> getComposition() {
        return composition;
    }

    public void setBlock(Material material, int percent) {
        if (material == null) return;

        if (percent <= 0) {
            composition.remove(material);
        } else {
            composition.put(material, percent);
        }

        rebuildCache();
    }

    public void removeBlock(Material material) {
        composition.remove(material);
        rebuildCache();
    }

    public int getTotalCompositionPercent() {
        int total = 0;

        for (int value : composition.values()) {
            total += value;
        }

        return total;
    }

    public Material getRandomMaterial() {
        if (cachedBlocks.isEmpty()) {
            return Material.STONE;
        }

        int index = random.nextInt(cachedBlocks.size());
        return cachedBlocks.get(index);
    }

    private void rebuildCache() {
        cachedBlocks.clear();

        for (Map.Entry<Material, Integer> entry : composition.entrySet()) {
            Material material = entry.getKey();
            int percent = entry.getValue();

            for (int i = 0; i < percent; i++) {
                cachedBlocks.add(material);
            }
        }
    }

    public int getBrokenBlocks() {
        return brokenBlocks;
    }

    public void addBrokenBlock() {
        this.brokenBlocks++;
    }

    public void resetBrokenBlocks() {
        this.brokenBlocks = 0;
    }

    public void resetStats() {
        this.brokenBlocks = 0;
        this.resetting = false;
    }

    public boolean contains(Location location) {
        if (location == null || pos1 == null || pos2 == null) return false;
        if (location.getWorld() == null || pos1.getWorld() == null || pos2.getWorld() == null) return false;
        if (!location.getWorld().getName().equalsIgnoreCase(pos1.getWorld().getName())) return false;

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());

        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());

        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return x >= minX && x <= maxX
                && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ;
    }

    public double getBrokenPercentage() {
        if (getVolume() <= 0) return 0.0;
        return (brokenBlocks * 100.0) / getVolume();
    }

    public int getResetPercent() {
        return resetPercent;
    }

    public void setResetPercent(int resetPercent) {
        this.resetPercent = resetPercent;
    }

    public int getResetDelay() {
        return resetDelay;
    }

    public void setResetDelay(int resetDelay) {
        this.resetDelay = resetDelay;
    }

    public boolean isResetting() {
        return resetting;
    }

    public void setResetting(boolean resetting) {
        this.resetting = resetting;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }
}