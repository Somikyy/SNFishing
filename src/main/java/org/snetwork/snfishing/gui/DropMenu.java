package org.snetwork.snfishing.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.utils.MessageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropMenu {
    private final SNFishing plugin;
    private static final int INVENTORY_SIZE = 54;
    private static final int ITEMS_PER_PAGE = 45;
    private final Map<Player, Integer> playerPages = new HashMap<>();

    public DropMenu(SNFishing plugin) {
        this.plugin = plugin;
    }

    public void openMenu(Player player) {
        openMenu(player, 1);
    }

    public void openMenu(Player player, int page) {
        playerPages.put(player, Integer.valueOf(page));
        Inventory inv = Bukkit.createInventory(null, INVENTORY_SIZE, "Fishing Drops - Page " + page);

        Map<String, ItemStack> items = plugin.getDropConfig().getItemDrops();
        List<ItemStack> displayItems = new ArrayList<>();

        // Создаём ItemStack'и с отображением шансов
        for (Map.Entry<String, ItemStack> entry : items.entrySet()) {
            ItemStack displayItem = entry.getValue().clone();
            ItemMeta meta = displayItem.getItemMeta();
            if (meta != null) {
                List<String> lore = new ArrayList<>();
                lore.add("§7Шанс: §e" + plugin.getDropConfig().getItemChance(entry.getKey()) + "%");
                lore.add("");
                lore.add("§eЛКМ §7- Увеличить шанс на 1%");
                lore.add("§eПКМ §7- Уменьшить шанс на 1%");
                lore.add("§eШИФТ + ПКМ §7- Удалить предмет");
                lore.add("§eШИФТ + ЛКМ §7- Установить редкость");
                meta.setLore(lore);
                displayItem.setItemMeta(meta);
            }
            displayItems.add(displayItem);
        }

        // Размещаем предметы с учётом пагинации
        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, displayItems.size());

        for (int i = startIndex; i < endIndex; i++) {
            inv.setItem(i - startIndex, displayItems.get(i));
        }

        // Добавляем кнопки навигации и управления
        addNavigationButtons(inv, page, displayItems.size());
        player.openInventory(inv);
    }

    public void handleClick(Player player, ItemStack clickedItem, ClickType clickType, int slot) {
        if (slot >= ITEMS_PER_PAGE) return; // Игнорируем клики по кнопкам навигации

        String itemKey = findItemKey(clickedItem);
        if (itemKey == null) return;

        switch (clickType) {
            case LEFT:
                // Увеличить шанс на 1%
                plugin.getDropConfig().increaseChance(itemKey);
                MessageUtils.sendSuccessMessage(player, "Шанс увеличен!");
                break;
            case RIGHT:
                // Уменьшить шанс на 1%
                plugin.getDropConfig().decreaseChance(itemKey);
                MessageUtils.sendSuccessMessage(player, "Шанс уменьшен!");
                break;
            case SHIFT_RIGHT:
                // Удалить предмет
                plugin.getDropConfig().removeItem(itemKey);
                MessageUtils.sendSuccessMessage(player, "Предмет удалён!");
                break;
            case SHIFT_LEFT:
                // Можно добавить установку редкости
                MessageUtils.sendSuccessMessage(player, "Функция редкости в разработке!");
                break;
        }

        // Обновляем меню
        openMenu(player, getCurrentPage(player));
    }

    private String findItemKey(ItemStack item) {
        if (item == null) return null;
        Map<String, ItemStack> items = plugin.getDropConfig().getItemDrops();
        for (Map.Entry<String, ItemStack> entry : items.entrySet()) {
            if (entry.getValue().isSimilar(item)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void addNavigationButtons(Inventory inv, int currentPage, int totalItems) {
        int maxPages = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);

        if (currentPage > 1) {
            inv.setItem(45, createGuiItem(Material.ARROW, "§eПредыдущая страница"));
        }

        if (currentPage < maxPages) {
            inv.setItem(53, createGuiItem(Material.ARROW, "§eСледующая страница"));
        }

        inv.setItem(49, createGuiItem(Material.ZOMBIE_SPAWN_EGG, "§aДобавить моба"));
        inv.setItem(50, createGuiItem(Material.EMERALD, "§aСохранить"));
    }

    private ItemStack createGuiItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    public int getCurrentPage(Player player) {
        return playerPages.getOrDefault(player, Integer.valueOf(1)).intValue();
    }
}