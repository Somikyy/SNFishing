package org.snetwork.snfishing.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.gui.DropMenu;

public class GUIListener implements Listener {
    private final SNFishing plugin;
    private final DropMenu dropMenu;

    public GUIListener(SNFishing plugin, DropMenu dropMenu) {
        this.plugin = plugin;
        this.dropMenu = dropMenu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().contains("Fishing Drops")) {
            return;
        }

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        ItemStack clickedItem = event.getCurrentItem();

        if (slot == 45 && clickedItem != null && clickedItem.getItemMeta() != null &&
                clickedItem.getItemMeta().getDisplayName().contains("Предыдущая")) {
            int currentPage = dropMenu.getCurrentPage(player);
            if (currentPage > 1) {
                dropMenu.openMenu(player, currentPage - 1);
            }
            return;
        }

        if (slot == 53 && clickedItem != null && clickedItem.getItemMeta() != null &&
                clickedItem.getItemMeta().getDisplayName().contains("Следующая")) {
            dropMenu.openMenu(player, dropMenu.getCurrentPage(player) + 1);
            return;
        }

        if (slot == 49) {
            // TODO: Implement mob selection menu
            player.sendMessage("§eМеню выбора мобов скоро будет добавлено!");
            return;
        }

        if (slot == 50) {
            plugin.getConfigManager().saveDropsConfig();
            player.sendMessage("§aКонфигурация сохранена!");
            return;
        }

        if (slot < 45 && clickedItem != null) {
            dropMenu.handleClick(player, clickedItem, event.getClick(), slot);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().contains("Fishing Drops")) {
            plugin.getConfigManager().saveDropsConfig();
        }
    }
}