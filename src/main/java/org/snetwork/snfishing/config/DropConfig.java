package org.snetwork.snfishing.config;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DropConfig {

    @Getter
    private final Map<String, ItemStack> itemDrops = new HashMap<>();
    @Getter
    private final Map<String, Integer> itemChances = new HashMap<>();
    @Getter
    private final Map<String, Double> mobDrops = new HashMap<>();
    private final ConfigManager configManager;

    public DropConfig(ConfigManager configManager) {
        this.configManager = configManager;
        loadDrops();
    }

    private void loadDrops() {
        ConfigurationSection itemsSection = configManager.getDropsConfig().getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    String materialName = itemSection.getString("material");
                    int amount = itemSection.getInt("amount", 1);
                    String displayName = itemSection.getString("displayname");
                    Integer chance = Integer.valueOf(itemSection.getInt("chance", 10)); // Шанс по умолчанию 10%

                    if (materialName != null) {
                        try {
                            Material material = Material.valueOf(materialName.toUpperCase());
                            ItemStack item = new ItemStack(material, amount);

                            if (displayName != null) {
                                ItemMeta meta = item.getItemMeta();
                                if (meta != null) {
                                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
                                    item.setItemMeta(meta);
                                }
                            }

                            itemDrops.put(key, item);
                            itemChances.put(key, chance);
                        } catch (IllegalArgumentException e) {
                            configManager.getPlugin().getLogger().warning("Invalid material name: " + materialName);
                        }
                    }
                }
            }
        }

        ConfigurationSection mobsSection = configManager.getDropsConfig().getConfigurationSection("mobs");
        if (mobsSection != null) {
            for (String key : mobsSection.getKeys(false)) {
                Double chance = Double.valueOf(mobsSection.getDouble(key, 0.0));
                mobDrops.put(key, chance);
            }
        }
    }

    public void increaseChance(String key) {
        Integer currentChance = itemChances.getOrDefault(key, Integer.valueOf(0));
        if (currentChance < 100) {
            itemChances.put(key, Integer.valueOf(currentChance + 1));
            saveChanges();
        }
    }

    public void decreaseChance(String key) {
        Integer currentChance = itemChances.getOrDefault(key, Integer.valueOf(0));
        if (currentChance > 0) {
            itemChances.put(key, Integer.valueOf(currentChance - 1));
            saveChanges();
        }
    }

    public void removeItem(String key) {
        itemDrops.remove(key);
        itemChances.remove(key);
        saveChanges();
    }

    public void addItem(String key, ItemStack item, int chance) {
        itemDrops.put(key, item);
        itemChances.put(key, Integer.valueOf(chance));
        saveChanges();
    }

    private void saveChanges() {
        ConfigurationSection itemsSection = configManager.getDropsConfig().getConfigurationSection("items");
        if (itemsSection != null) {
            for (String key : itemDrops.keySet()) {
                Integer chance = itemChances.get(key);
                if (chance != null) {
                    itemsSection.set(key + ".chance", Optional.of(chance.intValue()));
                }
            }
        }
        configManager.saveDropsConfig();
    }

    public int getItemChance(String key) {
        return itemChances.getOrDefault(key, Integer.valueOf(0)).intValue();
    }
}