package br.com.mixedmines.platform;

import org.bukkit.Material;

/**
 * Resolves configured block names across legacy and modern Bukkit versions.
 * Use names rather than numeric IDs: numeric IDs became ambiguous and were
 * removed from modern Minecraft.
 */
public final class MaterialResolver {

    private MaterialResolver() {
    }

    public static Material match(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        String name = input.trim().toUpperCase().replace(' ', '_').replace('-', '_');
        Material material = Material.matchMaterial(name);
        if (material != null) {
            return material;
        }

        // Backwards-friendly aliases for common configuration names.
        if ("GRASS".equals(name)) {
            return Material.matchMaterial("GRASS_BLOCK") != null
                    ? Material.matchMaterial("GRASS_BLOCK") : Material.matchMaterial("GRASS");
        }
        if ("WOOD".equals(name)) {
            return Material.matchMaterial("OAK_PLANKS") != null
                    ? Material.matchMaterial("OAK_PLANKS") : Material.matchMaterial("WOOD");
        }
        if ("LOG".equals(name)) {
            return Material.matchMaterial("OAK_LOG") != null
                    ? Material.matchMaterial("OAK_LOG") : Material.matchMaterial("LOG");
        }
        return null;
    }
}
