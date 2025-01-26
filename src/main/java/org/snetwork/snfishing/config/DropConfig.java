package org.snetwork.snfishing.config;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DropConfig {

    @Getter
    private final Map<String, ItemStack> itemDrops = new HashMap<>();
    @Getter
    private final Map<String, Double> mobDrops = new HashMap<>();

    public DropConfig(ConfigManager configManager) {
        loadDrops(configManager);
    }

    private void loadDrops(ConfigManager configManager) {
        ConfigurationSection itemsSection = configManager.getDropsConfig().getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ItemStack item = itemsSection.getItemStack(key);
                if (item != null) {
                    itemDrops.put(key, item);
                }
            }
        }

        ConfigurationSection mobsSection = configManager.getDropsConfig().getConfigurationSection("mobs");
        if (mobsSection != null) {
            for (String key : mobsSection.getKeys(false)) {
                double chance = mobsSection.getDouble(key);
                mobDrops.put(key, chance);
            }
        }
    }
}