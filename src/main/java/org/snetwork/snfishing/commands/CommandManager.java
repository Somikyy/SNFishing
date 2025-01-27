package org.snetwork.snfishing.commands;

import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

    private final SNFishing plugin;

    public CommandManager(SNFishing plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cЭта команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("snfishing.use")) {
            MessageUtils.sendErrorMessage(player, "§cУ вас нет прав на использование этой команды!");
            return true;
        }

        if (args.length == 0 || args[0].equalsIgnoreCase("gui")) {
            plugin.getDropMenu().openMenu(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (player.hasPermission("snfishing.admin")) {
                    plugin.getConfigManager().loadConfigs();
                    MessageUtils.sendSuccessMessage(player, "§aКонфигурация перезагружена!");
                } else {
                    MessageUtils.sendErrorMessage(player, "§cУ вас нет прав на использование этой команды!");
                }
                break;
            default:
                MessageUtils.sendErrorMessage(player, "§cИспользование: /snfishing [gui|reload]");
                break;
        }

        return true;
    }
}