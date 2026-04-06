package br.com.mixedmines.command;

import br.com.mixedmines.MixedMinesPlugin;
import br.com.mixedmines.manager.MineManager;
import br.com.mixedmines.model.Mine;
import br.com.mixedmines.selection.SelectionManager;
import br.com.mixedmines.util.LocationUtil;
import br.com.mixedmines.util.MineResetUtil;
import br.com.mixedmines.util.WandUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Map;

public class MixedMinesCommand implements CommandExecutor {

    private final MixedMinesPlugin plugin;

    public MixedMinesCommand(MixedMinesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("version")) {
            player.sendMessage("§e§lMIXEDMINES §7» §fVersão: §a" + plugin.getDescription().getVersion());
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("mixedmines.reload")) {
                player.sendMessage("§cVocê não tem permissão.");
                return true;
            }

            plugin.reloadPlugin();
            player.sendMessage("§aMixedMines recarregado com sucesso.");
            return true;
        }

        if (args[0].equalsIgnoreCase("wand")) {
            player.getInventory().addItem(WandUtil.createWand());
            player.sendMessage("§e§lMIXEDMINES §7» §aVocê recebeu a wand da mina.");
            return true;
        }

        if (args[0].equalsIgnoreCase("p1")) {
            SelectionManager.setPos1(player, player.getLocation());
            player.sendMessage("§aPosição 1 definida.");
            return true;
        }

        if (args[0].equalsIgnoreCase("p2")) {
            SelectionManager.setPos2(player, player.getLocation());
            player.sendMessage("§aPosição 2 definida.");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {

            if (args.length < 2) {
                player.sendMessage("§cUse: /mixedmines create <nome>");
                return true;
            }

            String mineName = args[1];

            if (MineManager.getMine(mineName) != null) {
                player.sendMessage("§cJá existe uma mina com esse nome.");
                return true;
            }

            Location pos1 = SelectionManager.getPos1(player);
            Location pos2 = SelectionManager.getPos2(player);

            if (pos1 == null || pos2 == null) {
                player.sendMessage("§cVocê precisa definir p1 e p2 primeiro.");
                return true;
            }

            Mine mine = new Mine(mineName, pos1, pos2);
            mine.setResetPercent(100);
            mine.setResetDelay(0);
            mine.setSilent(false);

            MineManager.addMine(mine);

            ConfigurationSection pos1Section = plugin.getMinesFileManager()
                    .getConfig()
                    .createSection("mines." + mineName + ".pos1");

            ConfigurationSection pos2Section = plugin.getMinesFileManager()
                    .getConfig()
                    .createSection("mines." + mineName + ".pos2");

            LocationUtil.saveLocation(pos1Section, pos1);
            LocationUtil.saveLocation(pos2Section, pos2);

            plugin.getMinesFileManager().getConfig().set("mines." + mineName + ".reset-percent", 100);
            plugin.getMinesFileManager().getConfig().set("mines." + mineName + ".reset-delay", 0);
            plugin.getMinesFileManager().getConfig().set("mines." + mineName + ".silent", false);
            plugin.getMinesFileManager().save();

            player.sendMessage("§aMina '" + mineName + "' criada com sucesso.");
            return true;
        }

        if (args[0].equalsIgnoreCase("redefine")) {

            if (args.length < 2) {
                player.sendMessage("§cUse: /mixedmines redefine <mina>");
                return true;
            }

            Mine mine = MineManager.getMine(args[1]);

            if (mine == null) {
                player.sendMessage("§cMina não encontrada.");
                return true;
            }

            Location pos1 = SelectionManager.getPos1(player);
            Location pos2 = SelectionManager.getPos2(player);

            if (pos1 == null || pos2 == null) {
                player.sendMessage("§cDefina p1 e p2 primeiro.");
                return true;
            }

            mine.setPos1(pos1);
            mine.setPos2(pos2);

            plugin.getMinesFileManager().getConfig().set("mines." + mine.getName() + ".pos1", null);
            plugin.getMinesFileManager().getConfig().set("mines." + mine.getName() + ".pos2", null);

            ConfigurationSection pos1Section = plugin.getMinesFileManager()
                    .getConfig()
                    .createSection("mines." + mine.getName() + ".pos1");

            ConfigurationSection pos2Section = plugin.getMinesFileManager()
                    .getConfig()
                    .createSection("mines." + mine.getName() + ".pos2");

            LocationUtil.saveLocation(pos1Section, pos1);
            LocationUtil.saveLocation(pos2Section, pos2);

            plugin.getMinesFileManager().save();

            player.sendMessage("§aÁrea da mina redefinida com sucesso.");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {

            if (MineManager.getMines().isEmpty()) {
                player.sendMessage("§cNenhuma mina criada.");
                return true;
            }

            player.sendMessage("§e§lMinas:");

            for (String mineName : MineManager.getMines().keySet()) {
                player.sendMessage("§7- §f" + mineName);
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {

            if (args.length < 2) {
                player.sendMessage("§cUse: /mixedmines info <nome>");
                return true;
            }

            Mine mine = MineManager.getMine(args[1]);

            if (mine == null) {
                player.sendMessage("§cA mina '" + args[1] + "' não existe.");
                return true;
            }

            player.sendMessage("§e§lMIXEDMINES §7» §fInformações da mina: §a" + mine.getName());
            player.sendMessage("§7Mundo: §f" + mine.getPos1().getWorld().getName());
            player.sendMessage("§7Pos1: §f" + mine.getPos1().getBlockX() + ", " + mine.getPos1().getBlockY() + ", " + mine.getPos1().getBlockZ());
            player.sendMessage("§7Pos2: §f" + mine.getPos2().getBlockX() + ", " + mine.getPos2().getBlockY() + ", " + mine.getPos2().getBlockZ());
            player.sendMessage("§7Tamanho: §f" + mine.getWidth() + " x " + mine.getHeight() + " x " + mine.getDepth());
            player.sendMessage("§7Volume: §f" + mine.getVolume() + " blocos");
            player.sendMessage("§7Quebrados: §f" + mine.getBrokenBlocks());
            player.sendMessage("§7Minerado: §f" + String.format("%.2f", mine.getBrokenPercentage()) + "%");
            player.sendMessage("§7Reseta com: §f" + mine.getResetPercent() + "%");
            player.sendMessage("§7Reset delay: §f" + mine.getResetDelay() + "s");
            player.sendMessage("§7Silent: §f" + mine.isSilent());
            player.sendMessage("§7Composição total: §f" + mine.getTotalCompositionPercent() + "%");

            if (mine.getComposition().isEmpty()) {
                player.sendMessage("§7Composição: §cNenhuma");
            } else {
                player.sendMessage("§7Composição:");
                for (Map.Entry<Material, Integer> entry : mine.getComposition().entrySet()) {
                    player.sendMessage("§8- §f" + entry.getKey().name() + " §7→ §e" + entry.getValue() + "%");
                }
            }

            return true;
        }

        if (args[0].equalsIgnoreCase("reset")) {

            if (args.length < 2) {
                player.sendMessage("§cUse: /mixedmines reset <nome>");
                return true;
            }

            Mine mine = MineManager.getMine(args[1]);

            if (mine == null) {
                player.sendMessage("§cA mina '" + args[1] + "' não existe.");
                return true;
            }

            MineResetUtil.resetMine(mine);
            player.sendMessage("§aMina '" + mine.getName() + "' resetada.");
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {

            if (args.length < 4) {
                player.sendMessage("§cUse: /mixedmines set <mina> <bloco> <percent>");
                return true;
            }

            Mine mine = MineManager.getMine(args[1]);

            if (mine == null) {
                player.sendMessage("§cMina não encontrada.");
                return true;
            }

            Material material = Material.matchMaterial(args[2].toUpperCase());

            if (material == null) {
                player.sendMessage("§cBloco inválido.");
                return true;
            }

            int percent;

            try {
                percent = Integer.parseInt(args[3]);
            } catch (Exception e) {
                player.sendMessage("§cPercent inválido.");
                return true;
            }

            if (percent < 1 || percent > 100) {
                player.sendMessage("§cA porcentagem deve estar entre 1 e 100.");
                return true;
            }

            int totalAtual = mine.getTotalCompositionPercent();
            int valorAntigo = 0;

            if (mine.getComposition().containsKey(material)) {
                valorAntigo = mine.getComposition().get(material);
            }

            int novoTotal = totalAtual - valorAntigo + percent;

            if (novoTotal > 100) {
                player.sendMessage("§cA composição total da mina não pode passar de 100%.");
                player.sendMessage("§7Atual: §f" + totalAtual + "%");
                player.sendMessage("§7Tentando definir §f" + material.name() + " §7para §f" + percent + "%");
                player.sendMessage("§7Novo total ficaria em: §f" + novoTotal + "%");
                return true;
            }

            mine.setBlock(material, percent);

            plugin.getMinesFileManager().getConfig().set(
                    "mines." + mine.getName() + ".composition." + material.name(),
                    percent
            );
            plugin.getMinesFileManager().save();

            player.sendMessage("§aBloco adicionado na composição da mina.");
            player.sendMessage("§7" + material.name() + " §f→ §e" + percent + "%");
            player.sendMessage("§7Composição total: §f" + mine.getTotalCompositionPercent() + "%");

            return true;
        }

        if (args[0].equalsIgnoreCase("unset")) {

            if (args.length < 3) {
                player.sendMessage("§cUse: /mixedmines unset <mina> <bloco>");
                return true;
            }

            Mine mine = MineManager.getMine(args[1]);

            if (mine == null) {
                player.sendMessage("§cMina não encontrada.");
                return true;
            }

            Material material = Material.matchMaterial(args[2].toUpperCase());

            if (material == null) {
                player.sendMessage("§cBloco inválido.");
                return true;
            }

            if (!mine.getComposition().containsKey(material)) {
                player.sendMessage("§cEsse bloco não está na composição da mina.");
                return true;
            }

            mine.removeBlock(material);

            plugin.getMinesFileManager().getConfig().set(
                    "mines." + mine.getName() + ".composition." + material.name(),
                    null
            );
            plugin.getMinesFileManager().save();

            player.sendMessage("§aBloco removido da composição da mina.");
            player.sendMessage("§7" + material.name() + " §fremovido.");
            player.sendMessage("§7Composição total: §f" + mine.getTotalCompositionPercent() + "%");

            return true;
        }

        if (args[0].equalsIgnoreCase("flag")) {

            if (args.length < 4) {
                player.sendMessage("§cUse: /mixedmines flag <mina> <flag> <valor>");
                return true;
            }

            Mine mine = MineManager.getMine(args[1]);

            if (mine == null) {
                player.sendMessage("§cMina não encontrada.");
                return true;
            }

            String flagName = args[2].toLowerCase();
            String value = args[3];

            if (flagName.equals("reset-percent")) {

                int percent;

                try {
                    percent = Integer.parseInt(value);
                } catch (Exception e) {
                    player.sendMessage("§cValor inválido.");
                    return true;
                }

                if (percent < 1 || percent > 100) {
                    player.sendMessage("§cO valor deve estar entre 1 e 100.");
                    return true;
                }

                mine.setResetPercent(percent);

                plugin.getMinesFileManager().getConfig().set(
                        "mines." + mine.getName() + ".reset-percent",
                        percent
                );
                plugin.getMinesFileManager().save();

                player.sendMessage("§aFlag atualizada com sucesso.");
                player.sendMessage("§7reset-percent §f→ §e" + percent + "%");
                return true;
            }

            if (flagName.equals("reset-delay")) {

                int delay;

                try {
                    delay = Integer.parseInt(value);
                } catch (Exception e) {
                    player.sendMessage("§cValor inválido.");
                    return true;
                }

                if (delay < 0) {
                    player.sendMessage("§cO valor deve ser 0 ou maior.");
                    return true;
                }

                mine.setResetDelay(delay);

                plugin.getMinesFileManager().getConfig().set(
                        "mines." + mine.getName() + ".reset-delay",
                        delay
                );
                plugin.getMinesFileManager().save();

                player.sendMessage("§aFlag atualizada com sucesso.");
                player.sendMessage("§7reset-delay §f→ §e" + delay + "s");
                return true;
            }

            if (flagName.equals("silent")) {

                boolean silent;

                if (value.equalsIgnoreCase("true")) {
                    silent = true;
                } else if (value.equalsIgnoreCase("false")) {
                    silent = false;
                } else {
                    player.sendMessage("§cUse true ou false.");
                    return true;
                }

                mine.setSilent(silent);

                plugin.getMinesFileManager().getConfig().set(
                        "mines." + mine.getName() + ".silent",
                        silent
                );
                plugin.getMinesFileManager().save();

                player.sendMessage("§aFlag atualizada com sucesso.");
                player.sendMessage("§7silent §f→ §e" + silent);
                return true;
            }

            player.sendMessage("§cFlag desconhecida.");
            return true;
        }

        player.sendMessage("§cSubcomando desconhecido. Use §e/mixedmines help");
        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage("§e§lMIXEDMINES §7» §fComandos disponíveis:");
        player.sendMessage("§8- §e/mixedmines help");
        player.sendMessage("§8- §e/mixedmines version");
        player.sendMessage("§8- §e/mixedmines reload");
        player.sendMessage("§8- §e/mixedmines wand");
        player.sendMessage("§8- §e/mixedmines p1");
        player.sendMessage("§8- §e/mixedmines p2");
        player.sendMessage("§8- §e/mixedmines create <nome>");
        player.sendMessage("§8- §e/mixedmines redefine <nome>");
        player.sendMessage("§8- §e/mixedmines list");
        player.sendMessage("§8- §e/mixedmines info <nome>");
        player.sendMessage("§8- §e/mixedmines reset <nome>");
        player.sendMessage("§8- §e/mixedmines set <mina> <bloco> <percent>");
        player.sendMessage("§8- §e/mixedmines unset <mina> <bloco>");
        player.sendMessage("§8- §e/mixedmines flag <mina> reset-percent <valor>");
        player.sendMessage("§8- §e/mixedmines flag <mina> reset-delay <segundos>");
        player.sendMessage("§8- §e/mixedmines flag <mina> silent <true/false>");
    }
}