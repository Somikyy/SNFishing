package org.snetwork.snfishing.events;

import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.fishing.FishingMechanics;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class FishingListener implements Listener {

    private final SNFishing plugin;
    private final FishingMechanics fishingMechanics;

    public FishingListener(SNFishing plugin) {
        this.plugin = plugin;
        this.fishingMechanics = plugin.getFishingMechanics();
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        fishingMechanics.handleFishing(event);
    }
}