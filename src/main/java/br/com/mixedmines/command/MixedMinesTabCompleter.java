package br.com.mixedmines.command;

import br.com.mixedmines.MixedMinesPlugin;
import br.com.mixedmines.manager.MineManager;
import br.com.mixedmines.model.Mine;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MixedMinesTabCompleter implements TabCompleter {

    @SuppressWarnings("unused")
    private final MixedMinesPlugin plugin;

    public MixedMinesTabCompleter(MixedMinesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if (!command.getName().equalsIgnoreCase("mixedmines")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            List<String> subcommands = new ArrayList<String>();
            subcommands.add("help");
            subcommands.add("version");
            subcommands.add("reload");
            subcommands.add("p1");
            subcommands.add("p2");
            subcommands.add("create");
            subcommands.add("redefine");
            subcommands.add("list");
            subcommands.add("info");
            subcommands.add("reset");
            subcommands.add("set");
            subcommands.add("unset");
            subcommands.add("flag");
            subcommands.add("wand");
            return filter(subcommands, args[0]);
        }

        if (args.length == 2) {
            String sub = args[0].toLowerCase();

            if (sub.equals("info") || sub.equals("reset") || sub.equals("set") || sub.equals("unset") || sub.equals("flag") || sub.equals("redefine")) {
                return filter(getMineNames(), args[1]);
            }
        }

        if (args.length == 3) {
            String sub = args[0].toLowerCase();

            if (sub.equals("set")) {
                return filter(getMaterialNames(), args[2]);
            }

            if (sub.equals("unset")) {
                Mine mine = MineManager.getMine(args[1]);
                if (mine == null) {
                    return Collections.emptyList();
                }
                return filter(getMineCompositionNames(mine), args[2]);
            }

            if (sub.equals("flag")) {
                List<String> flags = new ArrayList<String>();
                flags.add("reset-percent");
                flags.add("reset-delay");
                flags.add("silent");
                return filter(flags, args[2]);
            }
        }

        if (args.length == 4) {
            String sub = args[0].toLowerCase();

            if (sub.equals("flag")) {
                String flag = args[2].toLowerCase();

                if (flag.equals("reset-percent")) {
                    List<String> values = new ArrayList<String>();
                    values.add("10");
                    values.add("25");
                    values.add("50");
                    values.add("75");
                    values.add("100");
                    return filter(values, args[3]);
                }

                if (flag.equals("reset-delay")) {
                    List<String> values = new ArrayList<String>();
                    values.add("0");
                    values.add("1");
                    values.add("3");
                    values.add("5");
                    values.add("10");
                    return filter(values, args[3]);
                }

                if (flag.equals("silent")) {
                    List<String> values = new ArrayList<String>();
                    values.add("true");
                    values.add("false");
                    return filter(values, args[3]);
                }
            }

            if (sub.equals("set")) {
                List<String> values = new ArrayList<String>();
                values.add("1");
                values.add("5");
                values.add("10");
                values.add("20");
                values.add("25");
                values.add("50");
                values.add("75");
                values.add("100");
                return filter(values, args[3]);
            }
        }

        return Collections.emptyList();
    }

    private List<String> getMineNames() {
        List<String> names = new ArrayList<String>();

        for (String mineName : MineManager.getMines().keySet()) {
            names.add(mineName);
        }

        return names;
    }

    private List<String> getMineCompositionNames(Mine mine) {
        List<String> names = new ArrayList<String>();

        for (Map.Entry<Material, Integer> entry : mine.getComposition().entrySet()) {
            names.add(entry.getKey().name().toLowerCase());
        }

        return names;
    }

    private List<String> getMaterialNames() {
        List<String> materials = new ArrayList<String>();

        for (Material material : Material.values()) {
            if (material != null) {
                materials.add(material.name().toLowerCase());
            }
        }

        return materials;
    }

    private List<String> filter(List<String> input, String current) {
        List<String> result = new ArrayList<String>();

        for (String value : input) {
            if (value.toLowerCase().startsWith(current.toLowerCase())) {
                result.add(value);
            }
        }

        Collections.sort(result);
        return result;
    }
}