package org.snetwork.snfishing;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.snetwork.snfishing.commands.CommandManager;
import org.snetwork.snfishing.config.ConfigManager;
import org.snetwork.snfishing.config.DropConfig;
import org.snetwork.snfishing.events.FishingListener;
import org.snetwork.snfishing.fishing.FishingMechanics;
import org.snetwork.snfishing.fishing.FishingComboSystem;
import org.snetwork.snfishing.gui.DropMenu;
import org.snetwork.snfishing.listeners.GUIListener;

@Getter
public final class SNFishing extends JavaPlugin {

    private static SNFishing instance;
    private ConfigManager configManager;
    private DropConfig dropConfig;
    private FishingMechanics fishingMechanics;
    private FishingComboSystem comboSystem;
    private DropMenu dropMenu;

    public static SNFishing getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Initialize configurations
        configManager = new ConfigManager(this);
        dropConfig = new DropConfig(configManager);

        // Initialize mechanics
        fishingMechanics = new FishingMechanics(this);
        comboSystem = new FishingComboSystem(this);
        dropMenu = new DropMenu(this);

        // Register event listeners
        getServer().getPluginManager().registerEvents(new FishingListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this, dropMenu), this);

        // Register commands
        getCommand("snfishing").setExecutor(new CommandManager(this));

        getLogger().info("SNFishing enabled!");
    }

    @Override
    public void onDisable() {
        // Save configuration on disable
        configManager.saveDropsConfig();
        getLogger().info("SNFishing disabled!");
    }
}