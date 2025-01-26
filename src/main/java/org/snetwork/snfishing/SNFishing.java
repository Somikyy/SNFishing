package org.snetwork.snfishing;

import org.snetwork.snfishing.commands.CommandManager;
import org.snetwork.snfishing.config.ConfigManager;
import org.snetwork.snfishing.config.DropConfig;
import org.snetwork.snfishing.events.FishingListener;
import org.snetwork.snfishing.fishing.FishingMechanics;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class SNFishing extends JavaPlugin {

    @Getter
    private static SNFishing instance;

    @Getter
    private ConfigManager configManager;
    @Getter
    private DropConfig dropConfig;
    @Getter
    private FishingMechanics fishingMechanics;

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        dropConfig = new DropConfig(configManager);
        fishingMechanics = new FishingMechanics(this);

        getServer().getPluginManager().registerEvents(new FishingListener(this), this);
        getCommand("snfishing").setExecutor(new CommandManager(this));

        getLogger().info("SNFishing enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SNFishing disabled!");
    }
}