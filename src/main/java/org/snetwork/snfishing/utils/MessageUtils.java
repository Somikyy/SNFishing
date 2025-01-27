package org.snetwork.snfishing.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void sendMessage(Player player, String message, ChatColor color) {
        if (color != null) {
            message = color + message;
        }
        player.sendMessage(message);
    }

    public static void sendErrorMessage(Player player, String message) {
        sendMessage(player, message, ChatColor.RED);
    }

    public static void sendSuccessMessage(Player player, String message) {
        sendMessage(player, message, ChatColor.GREEN);
    }
}