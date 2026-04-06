package br.com.mixedmines.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class WandUtil {

    private static final String WAND_NAME = "§e§lMixedMines Wand";

    private WandUtil() {
    }

    public static ItemStack createWand() {
        ItemStack item = new ItemStack(Material.WOOD_AXE);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(WAND_NAME);

            List<String> lore = new ArrayList<String>();
            lore.add("§7Use para selecionar minas.");
            lore.add("§7Esquerdo: §fPos1");
            lore.add("§7Direito: §fPos2");
            meta.setLore(lore);

            item.setItemMeta(meta);
        }

        item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);

        ItemMeta finalMeta = item.getItemMeta();
        if (finalMeta != null) {
            finalMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(finalMeta);
        }

        return item;
    }

    public static boolean isMineWand(ItemStack item) {
        if (item == null) return false;
        if (item.getType() != Material.WOOD_AXE) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        if (!meta.hasDisplayName()) return false;

        return WAND_NAME.equals(meta.getDisplayName());
    }
}