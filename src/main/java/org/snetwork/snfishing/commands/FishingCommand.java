package org.snetwork.snfishing.commands;

import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.config.ConfigManager;
import org.snetwork.snfishing.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FishingCommand implements CommandExecutor {

    private final SNFishing plugin;
    private final ConfigManager configManager;

    public FishingCommand(SNFishing plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Эта команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            MessageUtils.sendErrorMessage(player, "Используйте: /snfishing additem <id> или /snfishing addmob <id> <chance>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "additem":
                // Логика добавления предмета
                break;
            case "addmob":
                // Логика добавления моба
                break;
            default:
                MessageUtils.sendErrorMessage(player, "Неизвестная команда!");
                break;
        }

        return true;
    }
}