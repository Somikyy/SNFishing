package org.snetwork.snfishing.events;

import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.config.DropConfig;
import org.snetwork.snfishing.utils.MessageUtils;
import org.snetwork.snfishing.utils.ParticleUtils;
import org.snetwork.snfishing.utils.SoundUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.Random;

public class RareEventListener implements Listener {

    private final SNFishing plugin;
    private final DropConfig dropConfig;
    private final Random random = new Random();

    public RareEventListener(SNFishing plugin) {
        this.plugin = plugin;
        this.dropConfig = plugin.getDropConfig();
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        Player player = event.getPlayer();

        if (random.nextDouble() < 0.1) { // 10% шанс на редкое событие
            MessageUtils.sendSuccessMessage(player, "§6Редкое событие! Вы поймали что-то особенное!");
            ParticleUtils.spawnParticle(player, Particle.FIREWORKS_SPARK, player.getLocation(), 30);
            SoundUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

            // Логика редкого дропа
            giveRareReward(player);
        }
    }

    private void giveRareReward(Player player) {
        // Пример редкого дропа
        player.getInventory().addItem(dropConfig.getItemDrops().get("diamond"));
    }
}