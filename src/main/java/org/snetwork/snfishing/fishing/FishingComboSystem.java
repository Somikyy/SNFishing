package org.snetwork.snfishing.fishing;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.snetwork.snfishing.SNFishing;
import org.snetwork.snfishing.utils.ParticleUtils;
import org.snetwork.snfishing.utils.SoundUtils;
import org.bukkit.Particle;
import org.bukkit.Sound;

import java.util.HashMap;
import java.util.Map;

public class FishingComboSystem {
    private final SNFishing plugin;
    private final Map<Player, Integer> comboCounts = new HashMap<>();
    private final Map<Player, BukkitRunnable> comboTimers = new HashMap<>();
    private static final int COMBO_TIMEOUT_SECONDS = 20;
    private static final int MIN_COMBO_FOR_DISPLAY = 3;

    public FishingComboSystem(SNFishing plugin) {
        this.plugin = plugin;
    }

    public void incrementCombo(Player player) {
        Integer currentCombo = comboCounts.getOrDefault(player, Integer.valueOf(0));
        Integer newCombo = Integer.valueOf(currentCombo.intValue() + 1);
        comboCounts.put(player, newCombo);

        // Отменяем существующий таймер если есть
        if (comboTimers.containsKey(player)) {
            comboTimers.get(player).cancel();
        }

        // Показываем эффекты комбо только если достигли минимального значения
        if (newCombo >= MIN_COMBO_FOR_DISPLAY) {
            showComboEffects(player, newCombo.intValue());
        }

        // Запускаем новый таймер
        BukkitRunnable comboTimer = new BukkitRunnable() {
            @Override
            public void run() {
                resetCombo(player);
            }
        };
        comboTimer.runTaskLater(plugin, COMBO_TIMEOUT_SECONDS * 20L);
        comboTimers.put(player, comboTimer);
    }

    private void showComboEffects(Player player, int combo) {
        // Показываем subtitle с комбо
        player.sendTitle("", getComboMessage(combo), 5, 20, 5);
        Location loc = player.getLocation();

        // Звуковые эффекты
        float pitch = Math.min(0.5f + (combo * 0.1f), 2.0f);

        if (combo >= 10) {
            // Максимальное комбо - супер эффекты
            ParticleUtils.spawnParticle(player, Particle.FLAME, loc, 30);
            ParticleUtils.spawnParticle(player, Particle.TOTEM, loc, 50);
            ParticleUtils.spawnParticle(player, Particle.PORTAL, loc, 20);
            SoundUtils.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1.0f);
            SoundUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);

            // Круг из частиц
            for (double i = 0; i < Math.PI * 2; i += Math.PI / 16) {
                double x = Math.cos(i) * 1.5;
                double z = Math.sin(i) * 1.5;
                Location particleLoc = loc.clone().add(x, 0.1, z);
                ParticleUtils.spawnParticle(player, Particle.FLAME, particleLoc, 1);
            }
        }
        else if (combo >= 8) {
            // Очень высокое комбо
            ParticleUtils.spawnParticle(player, Particle.TOTEM, loc, 40);
            ParticleUtils.spawnParticle(player, Particle.DRAGON_BREATH, loc, 20);
            SoundUtils.playSound(player, Sound.ENTITY_WITHER_SHOOT, 0.5f, 1.0f);
            SoundUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.8f);

            // Спираль
            for (double y = 0; y < 2; y += 0.1) {
                double x = Math.cos(y * 5) * (2 - y);
                double z = Math.sin(y * 5) * (2 - y);
                Location particleLoc = loc.clone().add(x, y, z);
                ParticleUtils.spawnParticle(player, Particle.TOTEM, particleLoc, 1);
            }
        }
        else if (combo >= 6) {
            // Высокое комбо
            ParticleUtils.spawnParticle(player, Particle.DRAGON_BREATH, loc, 30);
            ParticleUtils.spawnParticle(player, Particle.END_ROD, loc, 20);
            SoundUtils.playSound(player, Sound.ENTITY_BLAZE_SHOOT, 0.5f, 1.0f);
            SoundUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);

            // Двойная спираль
            for (double y = 0; y < 2; y += 0.2) {
                Location particleLoc1 = loc.clone().add(Math.cos(y * 4) * 0.8, y, Math.sin(y * 4) * 0.8);
                Location particleLoc2 = loc.clone().add(Math.cos((y * 4) + Math.PI) * 0.8, y, Math.sin((y * 4) + Math.PI) * 0.8);
                ParticleUtils.spawnParticle(player, Particle.DRAGON_BREATH, particleLoc1, 1);
                ParticleUtils.spawnParticle(player, Particle.DRAGON_BREATH, particleLoc2, 1);
            }
        }
        else if (combo >= 4) {
            // Среднее комбо
            ParticleUtils.spawnParticle(player, Particle.END_ROD, loc, 20);
            ParticleUtils.spawnParticle(player, Particle.VILLAGER_HAPPY, loc, 15);
            SoundUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);

            // Простая спираль
            for (double y = 0; y < 1.5; y += 0.2) {
                Location particleLoc = loc.clone().add(Math.cos(y * 4) * 0.6, y, Math.sin(y * 4) * 0.6);
                ParticleUtils.spawnParticle(player, Particle.END_ROD, particleLoc, 1);
            }
        }
        else {
            // Базовое комбо
            ParticleUtils.spawnParticle(player, Particle.VILLAGER_HAPPY, loc, 15);
            SoundUtils.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, pitch);
        }
    }

    private String getComboMessage(int combo) {
        if (combo < MIN_COMBO_FOR_DISPLAY) return "";

        String color;
        if (combo >= 10) color = "§4§l"; // Темно-красный и жирный для максимального комбо
        else if (combo >= 8) color = "§c"; // Красный
        else if (combo >= 6) color = "§6"; // Золотой
        else if (combo >= 4) color = "§e"; // Желтый
        else color = "§a"; // Зеленый

        return color + "Комбо x" + combo;
    }

    private Particle getComboParticle(int combo) {
        if (combo >= 10) return Particle.FLAME;
        else if (combo >= 8) return Particle.TOTEM;
        else if (combo >= 6) return Particle.DRAGON_BREATH;
        else if (combo >= 4) return Particle.END_ROD;
        else return Particle.VILLAGER_HAPPY;
    }

    public void resetCombo(Player player) {
        Integer currentCombo = comboCounts.get(player);
        if (currentCombo != null && currentCombo >= MIN_COMBO_FOR_DISPLAY) {
            player.sendTitle("", "§cКомбо сброшено!", 5, 20, 5);
        }

        comboCounts.remove(player);
        if (comboTimers.containsKey(player)) {
            comboTimers.get(player).cancel();
            comboTimers.remove(player);
        }
    }

    public int getCurrentCombo(Player player) {
        return comboCounts.getOrDefault(player, Integer.valueOf(0)).intValue();
    }
}