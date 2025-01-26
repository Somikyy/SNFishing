package org.snetwork.snfishing.config;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    @Getter
    private FileConfiguration dropsConfig;
    private File dropsFile;

    private final JavaPlugin plugin;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    private void loadConfigs() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        dropsFile = new File(plugin.getDataFolder(), "drops.yml");
        if (!dropsFile.exists()) {
            plugin.saveResource("drops.yml", false);
        }
        dropsConfig = YamlConfiguration.loadConfiguration(dropsFile);
    }

    public void saveDropsConfig() {
        try {
            dropsConfig.save(dropsFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save drops.yml!");
        }
    }
}