package org.snetwork.snfishing.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;

public class MessageUtils {

    public static void sendMessage(Player player, String message, TextColor color) {
        player.sendMessage(Component.text(message).color(color));
    }

    public static void sendErrorMessage(Player player, String message) {
        sendMessage(player, message, TextColor.color(255, 0, 0));
    }

    public static void sendSuccessMessage(Player player, String message) {
        sendMessage(player, message, TextColor.color(0, 255, 0));
    }
}